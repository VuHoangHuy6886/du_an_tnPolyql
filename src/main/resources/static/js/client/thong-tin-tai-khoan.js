function validateForm(event) {
    event.preventDefault(); // Ngăn chặn form gửi đi nếu có lỗi

    let ten = document.querySelector('[name="ten"]').value.trim();
    let soDienThoai = document.querySelector('[name="soDienThoai"]').value.trim();
    let phoneRegex = /^(03|05|07|08|09)[0-9]{8}$/;
    let errors = [];

    if (ten === "") {
        errors.push("Họ tên không được để trống.");
    }
    if (!phoneRegex.test(soDienThoai)) {
        errors.push("Số điện thoại không hợp lệ. Vui lòng nhập số di động Việt Nam (03, 05, 07, 08, 09).");
    }
    // Hiển thị lỗi nếu có
    if (errors.length > 0) {
        alert(errors.join("\n"));
        return false;
    }

    // Nếu không có lỗi, gửi form
    document.getElementById("myForm").submit();
}

