const GHN_TOKEN = "568f0428-ff0b-11ef-968a-1659e6af139f"; // Thay bằng API Token thực tế của bạn
const apiBase = "https://online-gateway.ghn.vn/shiip/public-api/master-data";
const customerId = document.getElementById("customerId").value
let mylist = document.getElementById("mylist")

function loadTinh() {
    $.ajax({
        url: `${apiBase}/province`,
        type: "GET",
        headers: {"Token": GHN_TOKEN},
        success: function (res) {
            if (res.code === 200) {
                res.data.forEach(item => {
                    $("#tinhCreate").append(`<option value="${item.ProvinceID}">${item.ProvinceName}</option>`);
                });
            }
        }
    });
}

function loadHuyen(tinhCreate) {
    $.ajax({
        url: `${apiBase}/district`,
        type: "POST",
        headers: {"Token": GHN_TOKEN, "Content-Type": "application/json"},
        data: JSON.stringify({province_id: parseInt(tinhCreate)}),
        success: function (res) {
            if (res.code === 200) {
                $("#huyenCreate").html('<option value="">Chọn quận/huyện</option>');
                res.data.forEach(item => {
                    $("#huyenCreate").append(`<option value="${item.DistrictID}">${item.DistrictName}</option>`);
                });
            }
        }
    });
}

function loadXa(huyenCreate) {
    $.ajax({
        url: `${apiBase}/ward`,
        type: "POST",
        headers: {"Token": GHN_TOKEN, "Content-Type": "application/json"},
        data: JSON.stringify({district_id: parseInt(huyenCreate)}),
        success: function (res) {
            if (res.code === 200) {
                $("#xaCreate").html('<option value="">Chọn phường/xã</option>');
                res.data.forEach(item => {
                    $("#xaCreate").append(`<option value="${item.WardCode}">${item.WardName}</option>`);
                });
            }
        }
    });
}

// Hàm lấy danh sách tỉnh/thành phố
async function getProvinceName(provinceId) {
    try {
        const response = await fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/province", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Token": GHN_TOKEN
            }
        });

        const data = await response.json();
        if (!data.data) throw new Error("Không có dữ liệu tỉnh/thành phố.");
        const province = data.data.find(p => p.ProvinceID == provinceId);
        return province ? province.ProvinceName : "Không xác định";
    } catch (error) {
        console.error("Lỗi lấy tỉnh/thành phố:", error);
        return "Lỗi lấy dữ liệu";
    }
}

// Hàm lấy danh sách quận/huyện
async function getDistrictName(districtId) {
    try {
        const response = await fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/district", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Token": GHN_TOKEN
            }
        });

        const data = await response.json();
        if (!data.data) throw new Error("Không có dữ liệu quận/huyện.");
        const district = data.data.find(d => d.DistrictID == districtId);
        return district ? district.DistrictName : "Không xác định";
    } catch (error) {
        console.error("Lỗi lấy quận/huyện:", error);
        return "Lỗi lấy dữ liệu";
    }
}

// Hàm lấy danh sách phường/xã
async function getWardName(districtId, wardId) {
    try {
        if (!districtId) throw new Error("Thiếu district_id!");

        const response = await fetch("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Token": GHN_TOKEN
            },
            body: JSON.stringify({district_id: parseInt(districtId)}) // 🔥 Quan trọng: Phải gửi số, không phải chuỗi!
        });

        const data = await response.json();
        if (!data.data) throw new Error("Không có dữ liệu phường/xã.");
        const ward = data.data.find(w => w.WardCode == wardId);
        return ward ? ward.WardName : "Không xác định";
    } catch (error) {
        console.error("Lỗi lấy phường/xã:", error);
        return "Lỗi lấy dữ liệu";
    }
}

// thêm địa chỉ
$(document).ready(function () {
    loadTinh();
    $("#tinhCreate").change(function () {
        let tinhCreate = $(this).val();
        if (tinhCreate) loadHuyen(tinhCreate);
        $("#huyenCreate").html('<option value="">Chọn quận/huyện</option>');
        $("#xaCreate").html('<option value="">Chọn phường/xã</option>');
    });

    $("#huyenCreate").change(function () {
        let huyenCreate = $(this).val();
        if (huyenCreate) loadXa(huyenCreate);
        $("#xaCreate").html('<option value="">Chọn phường/xã</option>');
    });

    $("#submit").click(function () {
        let tinhCreate = $("#tinhCreate").val();
        let huyenCreate = $("#huyenCreate").val();
        let xaCreate = $("#xaCreate").val();

        if (!tinhCreate || !huyenCreate || !xaCreate) {
            alert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        // Chỉ log mã của tỉnh, huyện, xã
        let addressDetail = document.getElementById("diaChiChiTietCreate").value
        let tenNgNhan = document.getElementById("tenNguoiNhan").value
        let soDienThoai = document.getElementById("soDienThoai").value
        console.log("TinhCreate ID:", tinhCreate);
        console.log("HuyenCreate ID:", huyenCreate);
        console.log("XaCreate ID:", xaCreate);
        console.log("CustomerId :", customerId);
        console.log("TenNgNhan:", tenNgNhan);
        console.log("SoDienThoai:", soDienThoai);
        console.log("dia chỉ chi tiết : ", addressDetail)
        const data = {
            customerID: customerId,  // Số nguyên, có thể để null nếu chưa có giá trị
            provinceID: tinhCreate,
            districtID: huyenCreate,
            wardID: xaCreate,
            addressStr: addressDetail,
            tenNguoiNhan: tenNgNhan,
            phone: soDienThoai
        };
        createAddress(data)
    });
});
// create
createAddress = (data) => {
    fetch("/api/create-address", {
        method: "POST",
        headers: {
            "content-type": "application/json",
        },
        body: JSON.stringify(data),
    }).then(res => res.json())
        .then(data => {
            console.log("phản hồi sau khi thêm", data);
        })
        .catch(err => console.log(err));
}
// find all
findAll = (id) => {
    fetch("/api/findAll-address-by-id-customer", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(id)
    }).then(res => res.json())
        .then(data => {
            data.forEach(item => {
                let id = item.id;
                let name = item.ten
                let phone = item.soDienThoai
                let ward = item.wardId
                let district = item.districtId
                let province = item.provinceId
                let defaultVL = item.defaultValue
                showAddress(id, name, phone, province, district, ward.toString(), defaultVL)
            })
        }).catch(err => console.log(err));
}
showAddress = async (id, name, phone, province, district, ward, defaultValue) => {
    let liTag = document.createElement("li");
    liTag.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-center");

    // Lấy dữ liệu từ API
    let provinceName = await getProvinceName(province);
    let districtName = await getDistrictName(district);
    let wardName = await getWardName(district, ward);

    // Tạo nội dung địa chỉ
    let info = document.createElement("div");
    info.innerHTML = `
        <strong>${name}</strong> - ${phone} <br>
        ${wardName} - ${districtName} - ${provinceName}
    `;

    // Nếu là mặc định, thêm badge hiển thị
    if (defaultValue) {
        let defaultBadge = document.createElement("span");
        defaultBadge.innerText = "Mặc định";
        defaultBadge.classList.add("badge", "bg-success", "ms-2");
        info.appendChild(defaultBadge);
    }

    // Tạo nhóm nút bấm
    let buttonGroup = document.createElement("div");
    buttonGroup.classList.add("btn-group");

    let editBtn = document.createElement("a");
    editBtn.innerText = "Sửa";
    editBtn.classList.add("btn", "btn-warning", "btn-sm");
    editBtn.href = "/showform-update/" + id; // Đặt link đến Google

    let deleteBtn = document.createElement("button");
    deleteBtn.innerText = "Xóa";
    deleteBtn.classList.add("btn", "btn-danger", "btn-sm");
    deleteBtn.onclick = () => deleteAddress(id);

    let defaultBtn = document.createElement("button");
    defaultBtn.innerText = "Chọn mặc định";
    defaultBtn.classList.add("btn", "btn-primary", "btn-sm");
    defaultBtn.onclick = () => setDefaultAddress(id);

    buttonGroup.appendChild(editBtn);
    buttonGroup.appendChild(deleteBtn);
    if (!defaultValue) {
        buttonGroup.appendChild(defaultBtn);
    }

    // Gắn vào `liTag`
    liTag.appendChild(info);
    liTag.appendChild(buttonGroup);

    mylist.appendChild(liTag);
};
findDiaChiById = async (id) => {
    try {
        const response = await fetch("/api/find-address-by-id-address", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(id)
        });

        const result = await response.json();
        return {
            id: result.id,
            province: result.provinceId,
            district: result.districtId,
            ward: result.wardId,
            ten: result.ten,
            sdt: result.soDienThoai,
            defaultValue: result.defaultValue
        };
    } catch (error) {
        console.error("Lỗi khi lấy địa chỉ:", error);
        return null;
    }
};

function deleteAddress(id) {
    fetch("/api/delete-address", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(id)
    }).then(res => res.text())
        .then(data => {
            alert(data)
            mylist.innerText = ""
            findAll(customerId)

        })
        .catch(err => console.log(err));
}

function setDefaultAddress(id) {
    fetch("/api/set-default-address", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(id)
    }).then(res => res.text())
        .then(data => {
            alert(data)
            mylist.innerText = ""
            findAll(customerId)
        })
        .catch(err => console.log(err));
}


document.addEventListener("DOMContentLoaded", function () {
    findAll(customerId)
})
