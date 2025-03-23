document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formAdd");
    const inputGiaTri = document.getElementById("ipValues");
    const errorGiaTri = document.getElementById("errorGiaTri");
    const thoiGianBatDau = document.getElementById("thoiGianBatDau");
    const thoiGianKetThuc = document.getElementById("thoiGianKetThuc");
    const percentRadio = document.getElementById("ckPercent");
    const dollarRadio = document.getElementById("ckDollar");

    function resetInput() {
        inputGiaTri.value = "";
        errorGiaTri.textContent = "";
        inputGiaTri.classList.remove("is-invalid");
    }

    function showError(element, message) {
        element.textContent = message;
        inputGiaTri.classList.add("is-invalid");
    }

    function hideError(element) {
        element.textContent = "";
        inputGiaTri.classList.remove("is-invalid");
    }

    function validateInput() {
        let value = inputGiaTri.value.trim();
        inputGiaTri.value = value.replace(/[^0-9]/g, '');

        if (value === "" || parseInt(value) < 1) {
            showError(errorGiaTri, "Giá trị phải là số nguyên dương từ 1 trở lên");
        } else if (percentRadio.checked && parseInt(value) > 100) {
            inputGiaTri.value = "100";
            hideError(errorGiaTri);
        } else {
            hideError(errorGiaTri);
        }
    }

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

    function validateForm(event) {
        validateInput();
        if (errorGiaTri.textContent !== "" || !validateDates()) {
            event.preventDefault();
            alert("Vui lòng kiểm tra lại các trường dữ liệu!");
        }
    }

    inputGiaTri.addEventListener("input", validateInput);
    percentRadio.addEventListener("click", resetInput);
    dollarRadio.addEventListener("click", resetInput);
    form.addEventListener("submit", validateForm);
});