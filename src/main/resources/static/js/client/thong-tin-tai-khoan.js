function validateForm(event) {
    event.preventDefault(); // Ngăn chặn form gửi đi nếu có lỗi

    let ten = document.querySelector('[name="ten"]').value.trim();
    let soDienThoai = document.querySelector('[name="soDienThoai"]').value.trim();
    let ngaySinh = document.querySelector('[name="ngaySinh"]').value.trim();
    let errors = [];

    let nameRegex = /^[\p{L}\s]+$/u;
    let phoneRegex = /^(03|05|07|08|09)[0-9]{8}$/;

    //ten
    if (ten === "") {
        errors.push("Họ tên không được để trống.");
    } else if (!nameRegex.test(ten)) {
        errors.push("Họ tên chỉ được chứa chữ cái và dấu cách.");
    } else if (!ten.replace(/\s/g, '').length) {
        errors.push("Họ tên không được chỉ chứa dấu cách.");
    }


    //sdt
    if (!phoneRegex.test(soDienThoai)) {
        errors.push("Số điện thoại không hợp lệ. Vui lòng nhập số di động Việt Nam (03, 05, 07, 08, 09).");
    }

    // Kiểm tra ngày sinh
    let birthDate = new Date(ngaySinh);
    let today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    let monthDiff = today.getMonth() - birthDate.getMonth();
    let dayDiff = today.getDate() - birthDate.getDate();

    if (!ngaySinh) {
        errors.push("Ngày sinh không được để trống.");
    } else if (age < 18 || (age === 18 && (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)))) {
        errors.push("Bạn phải đủ 18 tuổi trở lên.");
    }

    // Hiển thị lỗi nếu có
    if (errors.length > 0) {
        alert(errors.join("\n"));
        return false;
    }

    // Nếu không có lỗi, gửi form
    document.getElementById("myForm").submit();
}

