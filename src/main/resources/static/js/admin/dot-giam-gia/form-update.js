document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formAdd");
    const inputGiaTri = document.getElementById("ipValues");
    const errorGiaTri = document.getElementById("errorGiaTri");
    const thoiGianBatDau = document.getElementById("thoiGianBatDau");
    const thoiGianKetThuc = document.getElementById("thoiGianKetThuc");
    const percentRadio = document.getElementById("ckPercent");
    const dollarRadio = document.getElementById("ckDollar");
    const giamToiDa = document.getElementById("giamToiDa"); // Cột "Giảm tối đa"

    // 1️⃣ Reset giá trị input khi đổi radio button
    function resetInput() {
        inputGiaTri.value = "";
        errorGiaTri.textContent = "";
        inputGiaTri.classList.remove("is-invalid");
    }

    // 2️⃣ Hiển thị lỗi khi nhập sai
    function showError(element, message) {
        element.textContent = message;
        inputGiaTri.classList.add("is-invalid");
    }

    // 3️⃣ Ẩn lỗi khi nhập đúng
    function hideError(element) {
        element.textContent = "";
        inputGiaTri.classList.remove("is-invalid");
    }

    // 4️⃣ Kiểm tra giá trị nhập vào
    function validateInput() {
        let value = inputGiaTri.value.trim();
        inputGiaTri.value = value.replace(/[^0-9]/g, ''); // Chỉ cho phép số

        if (value === "" || parseInt(value) < 1) {
            showError(errorGiaTri, "Giá trị phải là số nguyên dương từ 1 trở lên");
        } else if (percentRadio.checked && parseInt(value) > 100) {
            inputGiaTri.value = "100";
            hideError(errorGiaTri);
        } else {
            hideError(errorGiaTri);
        }

        // Nếu chọn "Dollar", ẩn cột "Giảm tối đa"
        if (dollarRadio.checked) {
            giamToiDa.style.display = "none";
            giamToiDa.value = inputGiaTri.value; // Gán giá trị từ input
        } else {
            giamToiDa.style.display = ""; // Hiện lại nếu chọn Percent
        }
    }

    // 5️⃣ Kiểm tra ngày tháng hợp lệ
    function validateDates() {
        const startDate = new Date(thoiGianBatDau.value);
        const endDate = new Date(thoiGianKetThuc.value);
        const now = new Date();

        if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
            alert("Vui lòng nhập thời gian hợp lệ");
            return false;
        }

        if (startDate <= now) {
            alert("Thời gian bắt đầu phải sau thời gian hiện tại");
            return false;
        }

        if (startDate >= endDate) {
            alert("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
            return false;
        }
        return true;
    }

    // 6️⃣ Kiểm tra toàn bộ form trước khi gửi
    function validateForm(event) {
        validateInput();
        if (errorGiaTri.textContent !== "" || !validateDates()) {
            event.preventDefault();
            alert("Vui lòng kiểm tra lại các trường dữ liệu!");
        }
    }

    // 7️⃣ Ẩn/hiện "Giảm tối đa" khi trang load
    function handleVisibility() {
        if (dollarRadio.checked) {
            giamToiDa.style.display = "none"; // Ẩn khi chọn Dollar
        } else {
            giamToiDa.style.display = ""; // Hiện khi chọn Percent
        }
    }

    // Khi trang load lại, kiểm tra radio button đang chọn
    handleVisibility();

    // 8️⃣ Gán sự kiện động bằng `addEventListener`
    inputGiaTri.addEventListener("input", validateInput);
    percentRadio.addEventListener("click", function () {
        resetInput();
        handleVisibility();
    });

    dollarRadio.addEventListener("click", function () {
        resetInput();
        handleVisibility();
    });

    form.addEventListener("submit", validateForm);
});
