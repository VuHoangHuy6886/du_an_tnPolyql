let quantity = document.getElementById("inputQuantity");
let cartId = document.getElementById("idCart");
let tongTien = 0;
quantity.addEventListener("input", function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});

// quantity.addEventListener("blur", function () {
//     let value = Number(this.value);
//     if (isNaN(value) || value < 1) {
//         this.value = 1; // Nếu nhập sai, tự động sửa thành 1
//     }
//     console.log("Đã nhập xong: " + this.value);
//     let data = {
//         id: cartId.value,
//         quantity: this.value
//     }
//     sendServer(data);
// });

function sendServer(data) {
    fetch("/api/cart/update-quantity", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    setTimeout(() => {
        location.reload(true)
    }, 500)
}

document.addEventListener("DOMContentLoaded", function () {

    // Lấy giá trị customerId từ input hidden
    var customerId = document.getElementById("customerid").value;
    // Lấy thẻ <a> cần cập nhật
    var removeAllLink = document.getElementById("removeAllLink");
    // Cập nhật href của thẻ <a>
    removeAllLink.href = "/cart/remove-all/" + customerId;
    // handler checkbox
    const chonAll = document.getElementById("chonall")
    // reset lại form
    const chon = document.querySelectorAll(".chon")
    const totalPrices = document.getElementById("showTongTien");
    chon.forEach((item, index) => {
        item.checked = false;
    })

    function updateTotalPrice() {
        let totalPrice = 0
        let listCartId = []
        chon.forEach((item, index) => {
            if (item.checked) {
                // get price
                const priceString = document.querySelectorAll(".tongtien")[index].textContent
                const priceFloat = Number(priceString.replace(/,/g, '').replace(/[^\d.]/g, ''));
                totalPrice += priceFloat
                // get id cart
                let cartId = document.querySelectorAll(".idGioHang")[index].value;
                listCartId.push(cartId)
            }
        })
        // sử lý các phần trc kh thanh toán
        document.getElementById("cartIds").value = listCartId
        const idKhachHang = document.getElementById("idKhachHang").value;
        getVoucher({
            id: idKhachHang,
            tongTien: totalPrice
        })
        // hiển thị tre giao diện
        let convertToMoney = totalPrice.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
        totalPrices.innerText = convertToMoney;
        console.log("list cart have click : ",listCartId)

        tongTien = totalPrice;
        // sử lý button thanh toan
        if (listCartId.length > 0) {
            document.getElementById("btnThanToan").style.display = "block"
        } else {
            document.getElementById("btnThanToan").style.display = "none"
        }
    }

    chonAll.addEventListener("change", () => {
        chon.forEach(item => {
            item.checked = chonAll.checked
        })
        updateTotalPrice()
    })
    chon.forEach(item => {
        item.addEventListener("change", () => {
            chonAll.checked = [...chon].every(cb => cb.checked)
            updateTotalPrice()
        })

    })
});

let showVoucher = document.getElementById("showDanhSachVouCher");
showVoucher.innerText = "Vui Lòng Chọn Sản phẩm"

function getVoucher(data) {
    let arrVoucher = []

    fetch("/api/cart/get-voucher", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())  // Nhận phản hồi dạng json
        .then(result => {
            arrVoucher = result;
            console.log("Response from server:", arrVoucher);
            showVoucher.innerText = ''
            // Hiển thị danh sách voucher vào bảng
            showVoucher.innerHTML = arrVoucher.map(item => {
                return `
            <tr>
                <td>${item.ten}</td>
                <td>${item.giamToiDa} VND</td>
                <td>
                    <input type="radio" name="chonvoucher" class="voucher-radio"
                        value="${item.id}"
                        data-value="${item.giamToiDa}">
                </td>
            </tr>
        `;
            }).join("");
            handlerVoucher()
        })
        .catch(error => {
            console.error("Lỗi:", error);
        });
}

function handlerVoucher() {
    let selectedVoucherId = null;
    let selectedDiscountValue = null;

// Lắng nghe sự kiện khi chọn radio button
    document.querySelectorAll(".voucher-radio").forEach(radio => {
        radio.addEventListener("change", function () {
            selectedVoucherId = this.value;
            selectedDiscountValue = this.getAttribute("data-value");
            console.log(this.value)
        });
    });

    // Xử lý khi bấm nút OK
    document.getElementById("confirmVoucher").addEventListener("click", function () {
        if (selectedVoucherId) {
            console.log(`Voucher đã chọn: ID = ${selectedVoucherId}, Giá trị giảm = ${selectedDiscountValue} VND`);
            document.getElementById("voucherId").value = selectedVoucherId
            // Tính lại tiền
            let tongTienNew = tongTien - parseFloat(selectedDiscountValue);
            document.getElementById("soTienGiam").style.display = 'block';
            document.getElementById("soTienGiam").innerText = 'Số tiền giảm: ' +
                parseFloat(selectedDiscountValue).toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});

            document.getElementById("showTongTien").innerText = tongTienNew.toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND'
            });

        } else {
            console.log("Vui lòng chọn một voucher!");
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    let message = document.getElementById("messageRP")?.innerText;
    let success = document.getElementById("successRP")?.innerText;

    if (message && success) {
        console.log("message : ", message)
        Swal.fire({
            position: "top-end",
            icon: success.trim() === "true" ? "success" : "error",
            width: '400px',
            title: message,
            showConfirmButton: false,
            timer: 2000,
            customClass: {
                title: 'small-title', // Áp dụng CSS để giảm kích thước chữ
                popup: 'small-popup' // Áp dụng CSS để điều chỉnh padding
            }
        });
    }
});

