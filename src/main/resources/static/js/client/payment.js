const API_KEY = "568f0428-ff0b-11ef-968a-1659e6af139f";
const shopId = 5680488;
const shopDistrictId = 1452;
const shopWardId = 13010;
const totalQuantity = parseInt(document.getElementById("totalQuantity").value)
const canNang = totalQuantity * 100
const chieuCao = totalQuantity * 5
const customerId = document.getElementById("customerid").value;
const apiBase = "https://online-gateway.ghn.vn/shiip/public-api/master-data";
// object fill dia chi
const addressData = {
    id: '',
    province: '',
    district: '',
    ward: '',
    ten: '',
    sdt: '',
    defaultValue: ''
};
console.log("customer id : ", customerId)

// render ten dia chi ra view
async function displayAddress(addressData) {
    // showInformationAddress
    if (!addressData || !addressData.province || !addressData.district ||
        !addressData.ward || !addressData.ten || !addressData.sdt || !addressData.id) {
        let thongTin = document.getElementById("showMessage");
        thongTin.textContent = "Khách hàng không có địa chỉ mặc định"
        thongTin.style.color = "red"
        document.getElementById("showInformationAddress").style.display = "none"
        checkDiaChi()
    } else {
        try {
            document.getElementById("showInformationAddress").style.display = "block"
            document.getElementById("showMessage").style.display = "none";
            const provinceName = await getProvinceName(parseInt(addressData.province));
            const districtName = await getDistrictName(addressData.district);
            const wardName = await getWardName(addressData.district, addressData.ward);

            document.getElementById("province").textContent = provinceName;
            document.getElementById("district").textContent = districtName;
            document.getElementById("ward").textContent = wardName;
            document.getElementById("viewTen").textContent = addressData.ten;
            document.getElementById("viewSDT").textContent = addressData.sdt;
            //document.getElementById("addressID").textContent = addressData.id;

            let macDinh = addressData.defaultValue;
            let theSpan = document.getElementById("Default");

            if (theSpan) {
                if (macDinh) {
                    //theSpan.style.backgroundColor = "orange";
                    theSpan.style.color = "red";
                    theSpan.textContent = "Mặc định";
                } else {
                    // theSpan.style.backgroundColor = "#1cc88a";
                    theSpan.style.color = "orange";
                    theSpan.textContent = "Mới chọn";
                    //  theSpan.style.display = "none"
                }
            }

            // Hiển thị lên bill
            let billAddressInput = document.getElementById("billAddressID");
            if (billAddressInput) {
                billAddressInput.value = addressData.id;
            }
            checkDiaChi()
        } catch (error) {
            console.error("Lỗi khi xử lý địa chỉ:", error);
        }
    }
}

// lay id dia chi trong danh sach dia chi
function handlerClickAddress(e) {
    findDiaChiById(e);
}

// tim kiem dia chi theo id
async function findDiaChiById(id) {
    fetch("/api/find-address-by-id-address", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(id)
    }).then(response => response.json())
        .then(result => {
            let newAddress = {
                id: result.id,
                province: result.provinceId,
                district: result.districtId,
                ward: result.wardId,
                ten: result.ten,
                sdt: result.soDienThoai,
                defaultValue: result.defaultValue
            }
            Object.assign(addressData, newAddress)
            displayAddress(addressData)
            calculateShippingFee()
            checkDiaChi()
        })
        .catch(error => {
            console.error("Lỗi:", error);
        });
}

// tim kiem dia chi mac dinh theo id khach hang
async function findDiaChiByIdCustomer(customerId) {
    fetch("/api/find-address-by-id-customer", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(customerId)
    }).then(response => response.json())
        .then(result => {
            let newAddress = {
                id: result.id,
                province: result.provinceId,
                district: result.districtId,
                ward: result.wardId,
                ten: result.ten,
                sdt: result.soDienThoai,
                defaultValue: result.defaultValue
            }
            Object.assign(addressData, newAddress)
            displayAddress(addressData)
            calculateShippingFee()
        })
        .catch(error => {
            console.error("Lỗi:", error);
        });
}

// hien thi toan bo danh sach dia chi cua khach hang
async function findAllListDiaChiById(id) {
    console.log("hàm find all địa chỉ")
    try {
        const response = await fetch("/api/findAll-address-by-id-customer", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(id)
        });

        if (!response.ok) {
            throw new Error("Lỗi khi lấy danh sách địa chỉ");
        }
        console.log("ham find all list id : ", id)
        const listArrayAddress = await response.json();
        renderAddressList(listArrayAddress);
    } catch (error) {
        console.error("Lỗi:", error);
    }
}

// hien thi list dia chi cua khach hang len modal
function renderAddressList(addresses) {
    const addressContainer = document.getElementById("addressContainer");
    addressContainer.innerHTML = ""; // Xóa nội dung cũ
    if (addresses.length === 0) {
        addressContainer.innerHTML = "<p>Không có địa chỉ nào được tìm thấy.</p>";
        return;
    }
    console.log("địa chỉ render : ", addresses)
    addresses.forEach(ad => {
        const addressElement = document.createElement("div");
        addressElement.classList.add("rowAddress", "bg-warning");
        addressElement.style.padding = "10px";
        addressElement.style.color = "black"; // Đảm bảo chữ nhìn rõ trên nền xanh
        addressElement.style.borderBottom = "1px solid white";
        addressElement.innerHTML = `
            <span class="font-weight-bold">${ad.ten}</span> -
            <span class="font-weight-bold">${ad.soDienThoai}</span> :
            <input type="hidden" id="xaG" class="inputValues" value="${ad.wardId}">
            <span id="HienXaG">${ad.wardName || 'Không có xã'}</span> - 
            <input type="hidden" id="huyenG" class="inputValues" value="${ad.districtId}">
            <span id="HienHuyenG">${ad.districtName || 'Không có huyện'}</span> - 
            <input type="hidden" id="tinhG" class="inputValues" value="${ad.provinceId}">
            <span id="HienTinhG">${ad.provinceName || 'Không có tỉnh'}</span>
            <span>${ad.defaultValue ? " - mặc định" : ""}</span> 
            <input type="radio" value="${ad.id}" name="checkedAddress" onchange="handlerClickAddress(this.value)">
        `;
        addressContainer.appendChild(addressElement);
    });
    renderTextDiaChi()
}

//tinh phi ship
async function calculateShippingFee() {
    const url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Token": API_KEY,
            "ShopId": shopId.toString()
        },
        body: JSON.stringify({
            service_type_id: 2,
            from_district_id: shopDistrictId,
            to_district_id: parseInt(addressData.district),
            weight: canNang,
            length: 30,
            width: 30,
            height: chieuCao,
            insurance_value: 0,
            coupon: null
        })
    });

    const data = await response.json();
    if (data.code === 200) {
        document.getElementById("phiship").innerText = formatToVND(data.data.total);
        let phiShip = parseFloat(data.data.total);
        let tien = parseFloat(document.getElementById("tongTien").value);
        console.log("phi van chuyen : " + data.data.total);
        let tongTien = tien + phiShip;
        document.getElementById("totalPrice").innerText = formatToVND(tongTien)

        // hien thi bill
        document.getElementById("billShipping").value = phiShip;
        document.getElementById("billTotalPrice").value = tongTien;
    } else {
        console.error("Lỗi API GHN:", data);
    }
}


// hien thi tung element trong madal dia chi
function renderTextDiaChi() {
    const listDiaChi = document.querySelectorAll(".rowAddress");
    listDiaChi.forEach((items, index) => {
        // Lấy input và span tương ứng trong mỗi .rowAddress
        let tinhGInput = items.querySelector("#tinhG");
        let huyenGInput = items.querySelector("#huyenG");
        let xaGInput = items.querySelector("#xaG");

        let tinhGSpan = items.querySelector("#HienTinhG");
        let huyenGSpan = items.querySelector("#HienHuyenG");
        let xaGSpan = items.querySelector("#HienXaG");

        // Lấy giá trị từ input
        let tinhG = tinhGInput.value;
        let huyenG = huyenGInput.value;
        let xaG = xaGInput.value;
        // Gọi hàm cập nhật nội dung span
        displayAddressByList(tinhG, huyenG, xaG, tinhGSpan, huyenGSpan, xaGSpan);
    });
}

// chuyen tu ma so sang ten cua cac tinh thanh
async function displayAddressByList(t, h, x, tinhGSpan, huyenGSpan, xaGSpan) {
    // Giả sử đây là các hàm lấy tên từ API hoặc dữ liệu có sẵn
    const provinceName = await getProvinceName(t);
    const districtName = await getDistrictName(h);
    const wardName = await getWardName(h, x);

    // Cập nhật nội dung thẻ <span>
    tinhGSpan.innerText = provinceName;
    huyenGSpan.innerText = districtName;
    xaGSpan.innerText = wardName;
}

// thêm địa chỉ mới
$(document).ready(function () {
    loadTinhCreate();

    $("#tinhCreate").change(function () {
        let tinhCreate = $(this).val();
        if (tinhCreate) loadHuyenCreate(tinhCreate);
        $("#huyenCreate").html('<option value="">Chọn quận/huyện</option>');
        $("#xaCreate").html('<option value="">Chọn phường/xã</option>');
    });

    $("#huyenCreate").change(function () {
        let huyenCreate = $(this).val();
        if (huyenCreate) loadXaCreate(huyenCreate);
        $("#xaCreate").html('<option value="">Chọn phường/xã</option>');
    });

    $(document).ready(function () {
        $("#submit").click(function () {
            let isValid = true;

            let tenNguoiNhan = $("#tenNguoiNhanCreate").val().trim();
            let phone = $("#phoneNumberCreate").val().trim();
            let tinhCreate = $("#tinhCreate").val();
            let huyenCreate = $("#huyenCreate").val();
            let xaCreate = $("#xaCreate").val();
            let diaChi = $("#diaChiChiTietCreate").val().trim();

            // Reset error messages
            $(".text-danger").text("");

            // Kiểm tra Tên Người Nhận
            if (tenNguoiNhan === "") {
                $("#showErrorTen").text("Vui lòng nhập tên người nhận.");
                isValid = false;
            }

            // Kiểm tra Số Điện Thoại (phải là số điện thoại Việt Nam)
            let phonePattern = /^(?:\+84|0)(3[2-9]|5[2689]|7[0-9]|8[1-9]|9[0-9])\d{7}$/;
            if (phone === "") {
                $("#showErrorSDT").text("Vui lòng nhập số điện thoại.");
                isValid = false;
            } else if (!phonePattern.test(phone)) {
                $("#showErrorSDT").text("Số điện thoại không hợp lệ! Vui lòng nhập số điện thoại Việt Nam.");
                isValid = false;
            }

            // Kiểm tra Tỉnh/Thành phố
            if (!tinhCreate) {
                $("#showErrorTinh").text("Vui lòng chọn tỉnh/thành phố.");
                isValid = false;
            }

            // Kiểm tra Quận/Huyện
            if (!huyenCreate) {
                $("#showErrorHuyen").text("Vui lòng chọn quận/huyện.");
                isValid = false;
            }

            // Kiểm tra Phường/Xã
            if (!xaCreate) {
                $("#showErrorXa").text("Vui lòng chọn phường/xã.");
                isValid = false;
            }

            // Kiểm tra Địa Chỉ Chi Tiết
            if (diaChi === "") {
                $("#showErrorDiaChi").text("Vui lòng nhập địa chỉ chi tiết.");
                isValid = false;
            }

            // Nếu không có lỗi, gửi dữ liệu lên server
            if (isValid) {
                let data = {
                    provinceID: tinhCreate,
                    districtID: huyenCreate,
                    wardID: xaCreate,
                    addressStr: diaChi,
                    customerID: customerId,
                    tenNguoiNhan: tenNguoiNhan,
                    phone: phone
                };

                // Gọi API thêm địa chỉ
                addAddress(data);

                // Đóng modal sau khi thêm thành công
                $("#addAddressModal").modal("hide");
            }
        });
    });

});

// send dia chi vao server
function addAddress(data) {
    fetch("/api/create-address", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    }).then(response => response.text())
        .then(result => {
            console.log("add địa chỉ : ", result)
        })
        .catch(error => {
            console.error("Lỗi:", error);
        });
    setTimeout(() => {
        console.log("load lại data !!!!")
        window.location.reload(true);
    }, 500)
}

// su ly note cho shop
document.getElementById("ghiChuText").addEventListener("input", function () {
    let text = document.getElementById("ghiChuText").value
    document.getElementById("billNote").value = text
})
// SY LY PHUONG THUC THANH TOAN CHO BILL
document.addEventListener("DOMContentLoaded", () => {
    const btnChecks = document.querySelectorAll(".btn-check");
    const billPaymentMethod = document.getElementById("billPaymentMethod");
    const giamGiaElement = document.getElementById("giamGia");
    const soTienGiam = giamGiaElement
        ? convertVNDToNumber(giamGiaElement.textContent.trim()) || 0
        : 0;
    document.getElementById("billCoupon").value = soTienGiam;

    if (btnChecks.length > 0) {
        btnChecks[0].checked = true;  // Chọn mặc định nút đầu tiên
        billPaymentMethod.value = btnChecks[0].value; // Cập nhật giá trị
    }

    btnChecks.forEach(item => {
        item.addEventListener("change", () => {
            if (item.checked) {
                billPaymentMethod.value = item.value;
            }
        });
    });
});

// api giao hang nhanh
function loadTinhCreate() {
    $.ajax({
        url: `${apiBase}/province`,
        type: "GET",
        headers: {"Token": API_KEY},
        success: function (res) {
            if (res.code === 200) {
                res.data.forEach(item => {
                    $("#tinhCreate").append(`<option value="${item.ProvinceID}">${item.ProvinceName}</option>`);
                });
            }
        }
    });
}

function loadHuyenCreate(tinhCreate) {
    $.ajax({
        url: `${apiBase}/district`,
        type: "POST",
        headers: {"Token": API_KEY, "Content-Type": "application/json"},
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

function loadXaCreate(huyenCreate) {
    $.ajax({
        url: `${apiBase}/ward`,
        type: "POST",
        headers: {"Token": API_KEY, "Content-Type": "application/json"},
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
                "Token": API_KEY
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
                "Token": API_KEY
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
                "Token": API_KEY
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

// Hàm định dạng số thành tiền VNĐ
function formatToVND(amount) {
    return new Intl.NumberFormat("vi-VN").format(amount).replace(/\./g, ',') + " VNĐ";
}

function convertVNDToNumber(formattedVND) {
    return Number(formattedVND.replace(/[^0-9]/g, ""));
}

function checkDiaChi() {
    let diaChiCheck = document.getElementById("billAddressID").value
    if (diaChiCheck != null && diaChiCheck.trim().length > 0) {
        document.getElementById("btnDatHang").style.display = "block"
    } else {
        document.getElementById("btnDatHang").style.display = "none"
    }
}

findDiaChiByIdCustomer(customerId);
findAllListDiaChiById(customerId);
// confirm
document.getElementById("formDatHang").addEventListener("submit", function () {
    event.preventDefault(); // Ngăn form gửi đi ngay lập tức
    Swal.fire({
        title: "Bạn có muốn đặt hàng không?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        cancelButtonText: "Huỷ",
        confirmButtonText: "Đặt Hàng"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: "Đặt Hàng Thành Công!",
                text: "Đơn hàng của bạn đã được tạo!",
                icon: "success"
            }).then(() => {
                document.getElementById("formDatHang").submit(); // Gửi form khi xác nhận đặt hàng
            });
        }
    });
})