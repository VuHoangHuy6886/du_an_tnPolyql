const GHN_TOKEN = "568f0428-ff0b-11ef-968a-1659e6af139f"; // Thay bằng API Token thực tế của bạn
const apiBase = "https://online-gateway.ghn.vn/shiip/public-api/master-data";
const customerId = document.getElementById("customerId").value
let mylist = document.getElementById("mylist")

function loadTinh(selectId, selectedValue = "") {
    $.ajax({
        url: `${apiBase}/province`,
        type: "GET",
        headers: {"Token": GHN_TOKEN},
        success: function (res) {
            if (res.code === 200) {
                let select = $(`#${selectId}`);
                select.html('<option value="">Chọn tỉnh/thành phố</option>');
                res.data.forEach(item => {
                    let selected = item.ProvinceID == selectedValue ? "selected" : "";
                    select.append(`<option value="${item.ProvinceID}" ${selected}>${item.ProvinceName}</option>`);
                });
            } else {
                console.error("Lỗi khi load tỉnh:", res);
            }
        },
        error: function (xhr, status, error) {
            console.error("Lỗi API tỉnh:", error);
        }
    });
}

function loadHuyen(provinceId, selectId, selectedValue = "") {
    if (!provinceId) {
        console.warn("Không có tỉnh để load quận/huyện!");
        return;
    }

    $.ajax({
        url: `${apiBase}/district`,
        type: "POST",
        headers: {"Token": GHN_TOKEN, "Content-Type": "application/json"},
        data: JSON.stringify({province_id: parseInt(provinceId)}),
        success: function (res) {
            let select = $(`#${selectId}`);
            select.html('<option value="">Chọn quận/huyện</option>'); // Reset danh sách huyện
            $("#editXa").html('<option value="">Chọn phường/xã</option>'); // Reset xã khi tỉnh thay đổi

            if (res.code === 200) {
                res.data.forEach(item => {
                    let selected = item.DistrictID == selectedValue ? "selected" : "";
                    select.append(`<option value="${item.DistrictID}" ${selected}>${item.DistrictName}</option>`);
                });
            } else {
                console.error("Lỗi khi load quận/huyện:", res);
            }
        },
        error: function (xhr, status, error) {
            console.error("Lỗi API Quận/Huyện:", error);
        }
    });
}

function loadXa(districtId, selectId, selectedValue = "") {
    if (!districtId) {
        console.warn("Không có quận/huyện để load phường/xã!");
        return;
    }

    $.ajax({
        url: `${apiBase}/ward`,
        type: "POST",
        headers: {"Token": GHN_TOKEN, "Content-Type": "application/json"},
        data: JSON.stringify({district_id: parseInt(districtId)}),
        success: function (res) {
            let select = $(`#${selectId}`);
            select.html('<option value="">Chọn phường/xã</option>'); // Reset danh sách xã

            if (res.code === 200) {
                res.data.forEach(item => {
                    let selected = item.WardCode == selectedValue ? "selected" : "";
                    select.append(`<option value="${item.WardCode}" ${selected}>${item.WardName}</option>`);
                });
            } else {
                console.error("Lỗi khi load phường/xã:", res);
            }
        },
        error: function (xhr, status, error) {
            console.error("Lỗi API Phường/Xã:", error);
        }
    });
}

// load cho form add
async function loadTinhCr() {
    try {
        const res = await $.ajax({
            url: `${apiBase}/province`,
            type: "GET",
            headers: {"Token": GHN_TOKEN}
        });

        if (res.code === 200) {
            $("#tinhCreate").html('<option value="">Chọn tỉnh/thành</option>'); // Reset danh sách
            res.data.forEach(item => {
                $("#tinhCreate").append(`<option value="${item.ProvinceID}">${item.ProvinceName}</option>`);
            });
        }
    } catch (error) {
        console.error("Lỗi khi tải tỉnh/thành:", error);
    }
}

async function loadHuyenCr(provinceID) {
    if (!provinceID) return;

    try {
        const res = await $.ajax({
            url: `${apiBase}/district`,
            type: "POST",
            headers: {"Token": GHN_TOKEN, "Content-Type": "application/json"},
            data: JSON.stringify({province_id: parseInt(provinceID) || 0})
        });

        if (res.code === 200) {
            $("#huyenCreate").html('<option value="">Chọn quận/huyện</option>'); // Reset danh sách
            res.data.forEach(item => {
                $("#huyenCreate").append(`<option value="${item.DistrictID}">${item.DistrictName}</option>`);
            });
        }
    } catch (error) {
        console.error("Lỗi khi tải quận/huyện:", error);
    }
}

async function loadXaCr(districtID) {
    if (!districtID) return;

    try {
        const res = await $.ajax({
            url: `${apiBase}/ward`,
            type: "POST",
            headers: {"Token": GHN_TOKEN, "Content-Type": "application/json"},
            data: JSON.stringify({district_id: parseInt(districtID) || 0})
        });

        if (res.code === 200) {
            $("#xaCreate").html('<option value="">Chọn phường/xã</option>'); // Reset danh sách
            res.data.forEach(item => {
                $("#xaCreate").append(`<option value="${item.WardCode}">${item.WardName}</option>`);
            });
        }
    } catch (error) {
        console.error("Lỗi khi tải phường/xã:", error);
    }
}

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

createAddress = (data) => {
    fetch("/api/create-address", {
        method: "POST",
        headers: {
            "content-type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .finally(() => {
            // Luôn gọi lại findAll bất kể có lỗi hay không
            mylist.innerText = "";
            findAll(customerId);
        });
};


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
                let addressDetail = item.addressStr
                showAddress(id, name, phone, province, district, ward.toString(), defaultVL,addressDetail)
            })
        }).catch(err => console.log(err));
}
showAddress = async (id, name, phone, province, district, ward, defaultValue,addressDetail) => {
    let liTag = document.createElement("li");
    liTag.classList.add("list-group-item", "d-flex", "border", "justify-content-between", "align-items-center");

    // Lấy dữ liệu từ API
    let provinceName = await getProvinceName(province);
    let districtName = await getDistrictName(district);
    let wardName = await getWardName(district, ward);

    // Tạo nội dung địa chỉ
    let info = document.createElement("div");
    info.innerHTML = `
        <strong>${name}</strong> - ${phone} - ${addressDetail}<br>
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

    let editBtn = document.createElement("button");
    editBtn.innerText = "Sửa";
    editBtn.classList.add("btn", "btn-warning", "btn-sm");
    editBtn.onclick = () => editAddress(id);

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
            defaultValue: result.defaultValue,
            addressDetail: result.addressStr,
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

async function editAddress(id) {
    const address = await findDiaChiById(id);
    if (!address) {
        alert("Không tìm thấy địa chỉ!");
        return;
    }
    console.log("Dữ liệu địa chỉ cần sửa:", address);

    // Điền dữ liệu vào form sửa
    $("#editAddressId").val(address.id);
    $("#editTenNguoiNhan").val(address.ten);
    $("#editSoDienThoai").val(address.sdt);
    $("#editDiaChiChiTiet").val(address.addressDetail);

    // Chờ load tỉnh trước, sau đó load huyện và xã
    await loadTinh("editTinh", address.province);
    await loadHuyen(address.province, "editHuyen", address.district);
    await loadXa(address.district, "editXa", address.ward);

    $("#editAddressModal").modal("show");
}

$(document).ready(function () {
    loadTinhCr();

    $("#tinhCreate").change(function () {
        let tinhCreate = $(this).val();
        $("#huyenCreate").html('<option value="">Chọn quận/huyện</option>');
        $("#xaCreate").html('<option value="">Chọn phường/xã</option>');

        if (tinhCreate) loadHuyenCr(tinhCreate);
    });

    $("#huyenCreate").change(function () {
        let huyenCreate = $(this).val();
        $("#xaCreate").html('<option value="">Chọn phường/xã</option>');

        if (huyenCreate) loadXaCr(huyenCreate);
    });

    $("#submit").click(function (event) {
        event.preventDefault(); // Ngăn chặn submit form mặc định

        let isValid = true;
        $(".error-message").remove(); // Xóa thông báo lỗi trước đó

        let tenNgNhan = $("#tenNguoiNhan").val().trim();
        let soDienThoai = $("#soDienThoai").val().trim();
        let tinhCreate = $("#tinhCreate").val();
        let huyenCreate = $("#huyenCreate").val();
        let xaCreate = $("#xaCreate").val();
        let addressDetail = $("#diaChiChiTietCreate").val().trim();

        let phoneRegex = /^(0\d{9}|\+84\d{9})$/; // Kiểm tra số điện thoại Việt Nam

        if (!tenNgNhan) {
            isValid = false;
            $("#tenNguoiNhan").after('<p class="error-message text-danger">Vui lòng nhập tên người nhận!</p>');
        }

        if (!soDienThoai) {
            isValid = false;
            $("#soDienThoai").after('<p class="error-message text-danger">Vui lòng nhập số điện thoại!</p>');
        } else if (!phoneRegex.test(soDienThoai)) {
            isValid = false;
            $("#soDienThoai").after('<p class="error-message text-danger">Số điện thoại không hợp lệ!</p>');
        }

        if (!tinhCreate) {
            isValid = false;
            $("#tinhCreate").after('<p class="error-message text-danger">Vui lòng chọn tỉnh/thành phố!</p>');
        }

        if (!huyenCreate) {
            isValid = false;
            $("#huyenCreate").after('<p class="error-message text-danger">Vui lòng chọn quận/huyện!</p>');
        }

        if (!xaCreate) {
            isValid = false;
            $("#xaCreate").after('<p class="error-message text-danger">Vui lòng chọn phường/xã!</p>');
        }

        if (!addressDetail) {
            isValid = false;
            $("#diaChiChiTietCreate").after('<p class="error-message text-danger">Vui lòng nhập địa chỉ chi tiết!</p>');
        }

        // ✅ Nếu không hợp lệ, dừng lại và không đóng modal
        if (!isValid) return;

        const data = {
            customerID: customerId,
            provinceID: tinhCreate,
            districtID: huyenCreate,
            wardID: xaCreate,
            addressStr: addressDetail,
            tenNguoiNhan: tenNgNhan,
            phone: soDienThoai
        };
        // Gọi API lưu địa chỉ nếu hợp lệ
        createAddress(data);
        // ✅ Đóng modal chỉ khi dữ liệu hợp lệ
        $("#addressModal").modal("hide");
    });
});
$("#editTinh").change(function () {
    let provinceId = $(this).val();
    console.log("Tỉnh được chọn:", provinceId);

    if (provinceId) {
        loadHuyen(provinceId, "editHuyen");
        $("#editXa").html('<option value="">Chọn phường/xã</option>'); // Reset xã
    }
});
$("#editHuyen").change(function () {
    let districtId = $(this).val();
    console.log("Huyện được chọn:", districtId);

    if (districtId) {
        loadXa(districtId, "editXa");
    }
});
$("#saveEditAddress").click(function () {
    const tenNguoiNhan = $("#editTenNguoiNhan").val().trim();
    const soDienThoai = $("#editSoDienThoai").val().trim();
    const provinceID = $("#editTinh").val();
    const districtID = $("#editHuyen").val();
    const wardID = $("#editXa").val();
    const addressStr = $("#editDiaChiChiTiet").val().trim();

    // ✅ Kiểm tra rỗng
    if (!tenNguoiNhan) {
        alert("Vui lòng nhập Tên Người Nhận!");
        return;
    }
    if (!soDienThoai) {
        alert("Vui lòng nhập Số Điện Thoại!");
        return;
    }
    if (!provinceID) {
        alert("Vui lòng chọn Tỉnh/Thành phố!");
        return;
    }
    if (!districtID) {
        alert("Vui lòng chọn Quận/Huyện!");
        return;
    }
    if (!wardID) {
        alert("Vui lòng chọn Phường/Xã!");
        return;
    }
    if (!addressStr) {
        alert("Vui lòng nhập Địa chỉ chi tiết!");
        return;
    }

    // ✅ Kiểm tra định dạng số điện thoại Việt Nam (10 số, bắt đầu bằng 0)
    const phoneRegex = /^(0[3|5|7|8|9])([0-9]{8})$/;
    if (!phoneRegex.test(soDienThoai)) {
        alert("Số điện thoại không hợp lệ! Vui lòng nhập số điện thoại Việt Nam hợp lệ (VD: 0987654321)");
        return;
    }

    const updatedAddress = {
        customerID: customerId,
        id: $("#editAddressId").val(),
        tenNguoiNhan: tenNguoiNhan,
        phone: soDienThoai,
        provinceID: provinceID,
        districtID: districtID,
        wardID: wardID,
        addressStr: addressStr,
    };

    console.log("Dữ liệu cập nhật:", updatedAddress);

    fetch("/api/update-address", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(updatedAddress)
    })
        .then(res => res.text())
        .then(data => {
            alert("Cập nhật thành công!");
            $("#editAddressModal").modal("hide");
            mylist.innerText = ""; // Xóa danh sách cũ
            findAll(customerId); // Load lại danh sách
        })
        .catch(err => console.log("Lỗi khi cập nhật địa chỉ:", err));
});
document.addEventListener("DOMContentLoaded", function () {
    findAll(customerId)
})

