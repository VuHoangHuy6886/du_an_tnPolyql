let listIdProducts = [];
let listProductDetail = [];
let inputListIds = document.getElementById("productDetailIds");

// Sự kiện chọn tất cả sản phẩm chính
document.getElementById("checkAll").addEventListener("click", function () {
    let checkboxes = document.querySelectorAll("input[name='selectedProducts']");
    checkboxes.forEach((checkbox) => {
        checkbox.checked = this.checked;
        handlerClick(checkbox);
    });
});

// Hàm xử lý click từng sản phẩm chính
function handlerClick(checkbox) {
    if (checkbox.checked) {
        if (!listIdProducts.includes(checkbox.value)) {
            listIdProducts.push(checkbox.value);
        }
    } else {
        listIdProducts = listIdProducts.filter((item) => item !== checkbox.value);
    }

    console.log("Danh sách sản phẩm:", listIdProducts);
    sendListIdProducts(listIdProducts);

    // Cập nhật lại tất cả các link phân trang
    document.querySelectorAll(".page-link").forEach(item => {
        let url = new URL(item.href); // Dễ thao tác hơn với URL object

        // Xóa tham số cũ
        url.searchParams.delete("ids");

        // Lấy text từ input
        let text = document.getElementById("idsDC").value.trim();
        console.log("text : ", text);

        // Nếu text không rỗng thì xử lý thêm vào listIdProducts
        if (text !== "") {
            let mang = text.split(',').map(id => id.trim()).filter(id => id !== "");
            console.log("mảng tách từ text:", mang);

            // Gộp vào listIdProducts, đảm bảo không trùng lặp
            mang.forEach(id => {
                if (!listIdProducts.includes(id)) {
                    listIdProducts.push(id);
                }
            });
        }

        console.log("Danh sách listIdProducts cuối cùng:", listIdProducts);

        // Set lại tham số ids
        url.searchParams.set("ids", listIdProducts.join(","));

        // Gán lại href mới
        item.href = url.toString();
        sendListIdProducts(listIdProducts)
    });

}


// Hàm gửi danh sách sản phẩm đã chọn
function sendListIdProducts(listIdProducts) {
    let tablebodys = document.getElementById("tableBody");

    fetch("/admin/dot-giam-gia/listIdProducts", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(listIdProducts),
    })
        .then(response => {
            if (!response.ok) throw new Error(`Lỗi: ${response.status} - ${response.statusText}`);
            return response.json();
        })
        .then(data => {
            tablebodys.innerHTML = '';

            if (data.length > 0) {
                console.log("Dữ liệu trả về:", data);
                // Render bảng danh sách sản phẩm chi tiết
                console.log("data : ", data)
                data.forEach(item => {
                    let row = `<tr scope="row">
                    <td><input type="checkbox" class="productDetailCheck" value="${item.id}" onclick="handlerGetIdProductDetail(this)"></td>
                    <td>${item.maSanPham}</td>
                    <td>${item.tenSanPham}</td>
                    <td>${item.kichThuoc}</td>
                    <td>${item.mauSac}</td>
                    <td>${item.giaBan}</td></tr>`;
                    tablebodys.innerHTML += row;
                });

                // Xử lý sự kiện chọn tất cả sản phẩm chi tiết
                let checkedAllDetail = document.getElementById("checkedAllProductDetail");
                checkedAllDetail.addEventListener("change", function () {
                    let productDetailCheckboxes = document.querySelectorAll(".productDetailCheck");
                    listProductDetail = [];

                    productDetailCheckboxes.forEach(checkbox => {
                        checkbox.checked = checkedAllDetail.checked;
                        if (checkedAllDetail.checked) {
                            listProductDetail.push(checkbox.value);
                        }
                    });

                    updateProductDetailInput();
                    console.log("Danh sách chi tiết sản phẩm:", listProductDetail);
                });
            } else {
                tablebodys.innerHTML = '<h5 class="m-auto font-weight-bold text-danger">Sản phẩm không có biến thể nào</h5>';
            }
        })
        .catch(error => console.error("Lỗi:", error));
}

// Hàm xử lý khi chọn từng sản phẩm chi tiết
function handlerGetIdProductDetail(checkbox) {
    if (checkbox.checked) {
        if (!listProductDetail.includes(checkbox.value)) {
            listProductDetail.push(checkbox.value);
        }
    } else {
        listProductDetail = listProductDetail.filter(item => item !== checkbox.value);
    }
    updateProductDetailInput();
}

// Cập nhật giá trị input ẩn chứa danh sách sản phẩm chi tiết
function updateProductDetailInput() {
    inputListIds.value = listProductDetail.join(",");
    console.log("Danh sách sản phẩm chi tiết cập nhật:", inputListIds.value);
}

// handler gia tri giam and giam toi da
document.addEventListener("DOMContentLoaded", function () {
    const radioButtons = document.querySelectorAll('input[name="loaiChietKhau"]');
    const ipValues = document.getElementById("ipValues");
    const giamToiDa = document.getElementById("giamToiDa");
    console.log("id product đã chọn được : ",document.getElementById("idsDC").value)

    // Xử lý thay đổi radio
    function handleRadioChange() {
        const selectedValue = document.querySelector('input[name="loaiChietKhau"]:checked').value;
        ipValues.value = ""; // Reset giá trị khi chuyển đổi
        giamToiDa.value = ""; // Reset giá trị giảm tối đa khi chuyển đổi

        if (selectedValue === "PHAN_TRAM") {
            ipValues.placeholder = "phần trăm giảm";
            ipValues.removeEventListener("input", validateDollar);
            ipValues.addEventListener("input", validatePercentage);
            giamToiDaWrapper.style.display = "block"; // Hiển thị input giảm tối đa
        } else {
            ipValues.placeholder = "số tiền giảm";
            ipValues.removeEventListener("input", validatePercentage);
            ipValues.addEventListener("input", validateDollar);
            giamToiDaWrapper.style.display = "none"; // ẩn input giam toi da
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

        // Cập nhật giá trị giảm tối đa dựa trên phần trăm đã nhập
        // if (value !== "") {
        //     giamToiDa.value = value; // Giả sử giới hạn tối đa = % giảm, có thể sửa nếu có dữ liệu từ backend
        // } else {
        //     giamToiDa.value = "";
        // }
    }

    // Validate $: chỉ cho phép nhập số
    function validateDollar() {
        let value = ipValues.value.trim().replace(/\D/g, '');
        ipValues.value = value;

        // Khi nhập số tiền giảm thì cập nhật giá trị giảm tối đa bằng giá trị nhập vào
        giamToiDa.value = value;
    }

    // Gán sự kiện cho radio buttons
    radioButtons.forEach(radio => {
        radio.addEventListener("click", handleRadioChange);
    });

    // Thiết lập trạng thái mặc định khi tải trang
    handleRadioChange();
});
document.getElementById("formAdd").addEventListener("submit", function (event) {
    let isValid = true;
    let giaTriGiam = document.getElementById("ipValues").value.trim();
    let thoiGianBatDau = document.getElementById("thoiGianBatDau").value;
    let giamToiDa = document.getElementById("giamToiDa").value.trim();
    let thoiGianKetThuc = document.getElementById("thoiGianKetThuc").value;
    let listIdSanPhamChiTiet = document.getElementById("productDetailIds").value;
    let ten = document.getElementById("ten").value.trim();

    // Kiểm tra tên không được để trống
    if (ten === "") {
        document.getElementById("errorName").innerText = "Tên không được trống!";
        isValid = false;
    } else {
        document.getElementById("errorName").innerText = "";
    }

    // Kiểm tra giá trị giảm không được để trống & phải là số nguyên dương
    if (giaTriGiam === "" || !Number.isInteger(Number(giaTriGiam)) || Number(giaTriGiam) <= 0) {
        document.getElementById("showEr").innerText = "Giá trị giảm phải là số nguyên dương!";
        isValid = false;
    } else {
        document.getElementById("showEr").innerText = "";
    }

    // Kiểm tra giảm tối đa không được để trống & phải là số nguyên dương
    if (giamToiDa === "" || !Number.isInteger(Number(giamToiDa)) || Number(giamToiDa) <= 0) {
        document.getElementById("errorGiamToiDa").innerText = "Giảm tối đa phải là số nguyên dương!";
        isValid = false;
    } else {
        document.getElementById("errorGiamToiDa").innerText = "";
    }

    // Kiểm tra trường thời gian bắt đầu không được để trống
    if (thoiGianBatDau === "") {
        document.getElementById("showErStartDate").innerText = "Thời gian bắt đầu không được để trống";
        isValid = false;
    } else {
        document.getElementById("showErStartDate").innerText = "";
    }

    // Kiểm tra trường thời gian kết thúc không được để trống
    if (thoiGianKetThuc === "") {
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

    // Kiểm tra danh sách sản phẩm có được chọn không
    if (listIdSanPhamChiTiet === "") {
        alert("Vui lòng chọn sản phẩm chi tiết");
        isValid = false;
    }

    // Nếu có lỗi thì ngăn không cho submit form
    if (!isValid) {
        event.preventDefault();
    }
});