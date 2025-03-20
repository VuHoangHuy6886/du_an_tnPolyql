let listIdProducts = [];
let listProductDetail = [];
let inputListIds = document.getElementById("productDetailIds");
document.getElementById("checkAll").addEventListener("click", function () {
    let checkboxes = document.querySelectorAll("input[name='selectedProducts']");
    checkboxes.forEach((checkbox) => {
        checkbox.checked = this.checked;
        handlerClick(checkbox);
    });
});

function handlerClick(checkbox) {
    if (checkbox.checked) {
        console.log("Checked:", checkbox.value);
        listIdProducts.push(checkbox.value);
    } else {
        console.log("Unchecked:", checkbox.value);
        listIdProducts = listIdProducts.filter((item) => item !== checkbox.value);
    }
    console.log("list id products:", listIdProducts);
    sendListIdProducts(listIdProducts);
}

function sendListIdProducts(listIdProducts) {
    let tablebodys = document.getElementById("tableBody");
    fetch("/admin/dot-giam-gia/listIdProducts", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(listIdProducts),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Lỗi: ${response.status} - ${response.statusText}`);
            }
            return response.json();
        })
        .then(data => {
            tablebodys.innerHTML = '';
            //
            if (data.length >= 1) {
                console.log(data)
                data.map(item => {
                    let row = `<tr scope="row">
                            <td><input type="checkbox" value="${item.id}" onclick="handlerGetIdProductDetail(this)"></td>
                            <td>${item.kichThuoc.ten}</td>
                            <td>${item.mauSac.ten}</td>
                            <td>${item.giaBan}</td></tr>`;
                    tablebodys.innerHTML += row;
                })
            } else {
                tablebodys.innerHTML = '<h5 class="m-auto font-weight-bold text-danger">Sản phẩm không có biến thể nào</h5>'
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
        });
}

// function get id product detail
function handlerGetIdProductDetail(checkbox) {
    if (checkbox.checked) {
        if (!listProductDetail.includes(checkbox.value)) {
            listProductDetail.push(checkbox.value);
        }
    } else {
        listProductDetail = listProductDetail.filter(item => item !== checkbox.value);
    }
    console.log("product detail: ", listProductDetail);
    let productDetailIds = listProductDetail.join(",");
    inputListIds.value = productDetailIds;
}


// start  validated
document.addEventListener("DOMContentLoaded", function () {
    const radioButtons = document.querySelectorAll('input[name="loaiChietKhau"]');
    const ipValues = document.getElementById("ipValues");
    const discountForm = document.getElementById("discountForm");

    // Xử lý thay đổi radio
    function handleRadioChange() {
        const selectedValue = document.querySelector('input[name="loaiChietKhau"]:checked').value;
        ipValues.value = ""; // Reset giá trị khi chuyển đổi
        if (selectedValue === "PHAN_TRAM") {
            ipValues.placeholder = "phần trăm giảm";
            ipValues.removeEventListener("input", validateDollar);
            ipValues.addEventListener("input", validatePercentage);
        } else {
            ipValues.placeholder = "số tiền giảm";
            ipValues.removeEventListener("input", validatePercentage);
            ipValues.addEventListener("input", validateDollar);
        }
    }

    // Validate %: chỉ cho phép nhập số từ 1 đến 100
    function validatePercentage() {
        let value = ipValues.value.trim().replace(/\D/g, '');
        let numericValue = Number(value);
        if (numericValue > 100) {
            value = "100";
        } else if (numericValue < 1 && value !== "") {
            value = "1";
        }
        ipValues.value = value;
    }

    // Validate $: chỉ cho phép nhập số
    function validateDollar() {
        ipValues.value = ipValues.value.trim().replace(/\D/g, '');
        // su ly input giam toi da
        let discountMax = document.getElementById("giamToiDa")
        discountMax.value = ipValues.value
        discountMax.style.display = "none"
    }

    // Gán sự kiện cho radio buttons
    radioButtons.forEach(radio => {
        radio.addEventListener("click", handleRadioChange);
    });
    // Thiết lập trạng thái mặc định khi tải trang
    handleRadioChange();
});
document.getElementById("formAdd").addEventListener("submit", function (event) {
    let isValid = true
    let giaTriGiam = document.getElementById("ipValues").value;
    let thoiGianBatDau = document.getElementById("thoiGianBatDau").value;
    let thoiGianKetThuc = document.getElementById("thoiGianKetThuc").value;
    let listIdSanPhamChiTiet = document.getElementById("productDetailIds").value;
    if (giaTriGiam == "") {
        document.getElementById("showEr").innerText = "giá trị giảm không được để trống !";
        isValid = false;
    } else {
        document.getElementById("showEr").innerText = "";
    }
    // Kiểm tra trường thời gian bắt đầu không được để trống
    if (thoiGianBatDau == "") {
        document.getElementById("showErStartDate").innerText = "Thời gian bắt đầu không được để trống";
        isValid = false;
    } else {
        document.getElementById("showErStartDate").innerText = "";
    }

    // Kiểm tra trường thời gian kết thúc không được để trống
    if (thoiGianKetThuc == "") {
        document.getElementById("showErEndDate").innerText = "Thời gian kết thúc không được để trống";
        isValid = false;
    } else {
        document.getElementById("showErEndDate").innerText = "";
    }

    // So sánh thời gian: Ngày bắt đầu phải nhỏ hơn ngày kết thúc
    let dateBatDau = new Date(thoiGianBatDau);
    let dateKetThuc = new Date(thoiGianKetThuc);

    if (dateBatDau >= dateKetThuc) {
        document.getElementById("showErStartDate").innerText = "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc";
        isValid = false;
    }
    if (listIdSanPhamChiTiet == "") {
        alert("vui lòng chọn sản phẩm chi tiết");
        isValid = false;
    }
    if (!isValid) {
        event.preventDefault();
    }
})