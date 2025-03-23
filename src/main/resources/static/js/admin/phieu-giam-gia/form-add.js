let ids = [];

document.addEventListener("DOMContentLoaded", function () {
    let inputGiaTriGiam = document.querySelector("input[name='giaTriGiam']");
    let radioPhanTram = document.getElementById("phantram");
    let radioGiaTien = document.getElementById("giatien");
    let inputGiamToiDa = document.querySelector("input[name='giamToiDa']");

    // Khi nhập giá trị giảm
    inputGiaTriGiam.addEventListener("input", function () {
        let giaTri = parseFloat(inputGiaTriGiam.value) || 0;

        if (radioPhanTram.checked && giaTri > 99) {
            alert("Giá Trị Giảm không được lớn hơn 99%.");
            inputGiaTriGiam.value = 99;
        }

        console.log("Giá trị giảm nhập vào:", inputGiaTriGiam.value);

        // Nếu chọn VND thì cập nhật luôn giảm tối đa
        if (radioGiaTien.checked) {
            inputGiamToiDa.value = inputGiaTriGiam.value;
            console.log("Giảm tối đa khi nhập (VNĐ):", inputGiamToiDa.value);
        }
    });

    // Khi chọn %
    radioPhanTram.addEventListener("change", function () {
        inputGiaTriGiam.value = ''
        console.log("Chọn %");
        inputGiamToiDa.style.display = "block";
        inputGiamToiDa.value = ""; // Không gán giá trị giảm vào input giảm tối đa
        console.log("Giảm tối đa sau khi chọn %:", inputGiamToiDa.value);
    });

    // Khi chọn giá tiền
    radioGiaTien.addEventListener("change", function () {
        console.log("Chọn giá tiền");
        inputGiamToiDa.style.display = "none";
        inputGiamToiDa.value = inputGiaTriGiam.value; // Cập nhật ngay lập tức
        console.log("Giảm tối đa sau khi chọn giá tiền:", inputGiamToiDa.value);
    });
});



function toggleCheckbox(input) {
    let value = parseInt(input.value);
    if (input.checked) {
        ids.push(value);
    } else {
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


// Kiểm tra tên phiếu giảm giá
    let regexTen = /^[\p{L}0-9 %]+$/u;

    // Kiểm tra Tên
    if (ten === "") {
        alert("Tên không được để trống.");
        return false;
    }
    if (ten.replace(/\s/g, "") === "") {
        alert("Tên không được chỉ chứa dấu cách.");
        return false;
    }
    if (!regexTen.test(ten)) {
        alert("Tên chỉ được chứa chữ cái, số và ký tự '%'. Vui lòng nhập lại.");
        return false;
    }


// Kiểm tra giá trị giảm
    if (isNaN(giaTriGiam) || giaTriGiam <= 0) {
        alert("Giá Trị Giảm phải là số dương.");
        return false;
    }

// Kiểm tra giảm tối đa
    if (isNaN(giamToiDa) || giamToiDa <= 0 || !Number.isInteger(parseFloat(giamToiDa))) {
        alert("Giảm tối đa phải là số nguyên dương.");
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
    if (new Date(ngayKetThuc) <= new Date()) {
        alert("Ngày Kết Thúc phải sau thời gian hiện tại.");
        return false;
    }
    return true;
}