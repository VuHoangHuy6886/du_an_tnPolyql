document.addEventListener("DOMContentLoaded", function () {
    let inputGiaTriGiam = document.querySelector("input[name='giaTriGiam']");
    let radioPhanTram = document.getElementById("phantram");
    let radioGiaTien = document.getElementById("giatien");

    inputGiaTriGiam.addEventListener("input", function () {
        let giaTri = parseFloat(inputGiaTriGiam.value);

        // Kiểm tra nếu chọn %
        if (radioPhanTram.checked && giaTri > 99) {
            alert("Giá Trị Giảm không được lớn hơn 99%.");
            inputGiaTriGiam.value = 99; // Set về 99 nếu nhập quá
        }
    });

    // Nếu đổi radio button, xóa giới hạn nếu chọn VND
    document.querySelectorAll("input[name='loaiHinhGiam']").forEach(radio => {
        radio.addEventListener("change", function () {
            inputGiaTriGiam.value = ""; // Xóa input khi chuyển đổi
        });
    });
});

//lay du lieu tu check box costomList
let ids = [];

function toggleCheckbox(input) {
    let value = parseInt(input.value);
    if (input.checked) {
        ids.push(value);
    } else {
// Lọc id khách hàng, so sánh với mảng hiện tại, đã có >> bỏ đi
        ids = ids.filter(id => id !== value);
    }

    document.getElementById("listIdCustomer").value = '';
    console.log("thong tin khach hang : " + ids);
    document.getElementById("listIdCustomer").value = ids.join(",");
    console.log(document.getElementById("listIdCustomer").value)
}

function validateForm() {
    let ten = document.getElementById("ten").value.trim();
    let giaTriGiam = document.getElementById("loaiHinhGiam").value.trim();
    let giamToiDa = document.getElementById("giamToiDa").value.trim();
    let soLuong = document.getElementById("soLuong").value.trim();
    let hoaDonToiThieu = document.getElementById("hoaDonToiThieu").value.trim();
    let ngayBatDau = document.getElementById("ngayBatDau").value;
    let ngayKetThuc = document.getElementById("ngayKetThuc").value;

    let regexTen = /^[a-zA-ZÀ-Ỹà-ỹ\s]+$/; // Chỉ cho phép chữ và khoảng trắng

// Kiểm tra tên phiếu giảm giá
    if (ten === "" || !regexTen.test(ten)) {
        alert("Tên Phiếu Giảm Giá không được để trống và chỉ chứa chữ cái.");
        return false;
    }

// Kiểm tra giá trị giảm
    if (isNaN(giaTriGiam) || giaTriGiam <= 0) {
        alert("Giá Trị Giảm phải là số dương.");
        return false;
    }

// Kiểm tra giảm tối đa
    if (isNaN(giamToiDa) || giamToiDa < 0) {
        alert("Giảm Tối Đa phải là số dương.");
        return false;
    }

// Kiểm tra số lượng
    if (isNaN(soLuong) || soLuong <= 0 || !Number.isInteger(parseFloat(soLuong))) {
        alert("Số Lượng phải là số nguyên dương.");
        return false;
    }

// Kiểm tra hóa đơn tối thiểu
    if (isNaN(hoaDonToiThieu) || hoaDonToiThieu <= 0 || !Number.isInteger(parseFloat(hoaDonToiThieu))) {
        alert("Hóa Đơn Tối Thiểu phải là số dương.");
        return false;
    }

// Kiểm tra ngày bắt đầu và ngày kết thúc
    if (ngayBatDau === "" || ngayKetThuc === "") {
        alert("Vui lòng nhập Ngày Bắt Đầu và Ngày Kết Thúc.");
        return false;
    }
    if (new Date(ngayKetThuc) <= new Date(ngayBatDau)) {
        alert("Ngày Kết Thúc phải sau Ngày Bắt Đầu.");
        return false;
    }

    return true;
}