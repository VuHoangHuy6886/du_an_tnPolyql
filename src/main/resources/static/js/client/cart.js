let quantity = document.getElementById("inputQuantity");
let cartId = document.getElementById("idCart");
let tongTien = 0;
let totalPriceAfterDiscount = 0;
quantity.addEventListener("input", function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});

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
    var removeAllLink = document.getElementById("removeAllLink");
    removeAllLink.href = "/cart/remove-all/" + customerId;

    // Handler checkbox
    const chonAll = document.getElementById("chonall");
    const chon = document.querySelectorAll(".chon");
    const totalPrices = document.getElementById("showTongTien");

    // Reset form khi tải trang
    chon.forEach(item => item.checked = false);

    function updateTotalPrice() {
        let totalPrice = 0;
        let listCartId = [];

        chon.forEach((item, index) => {
            if (item.checked) {
                const priceString = document.querySelectorAll(".tongtien")[index].textContent;
                const priceFloat = Number(priceString.replace(/,/g, '').replace(/[^\d.]/g, ''));
                totalPriceAfterDiscount += priceFloat
                totalPrice += priceFloat;
                let cartId = document.querySelectorAll(".idGioHang")[index].value;
                listCartId.push(cartId);
            }
        });

        document.getElementById("cartIds").value = listCartId;

        // Chỉ lấy voucher nếu có sản phẩm được chọn
        if (listCartId.length > 0) {
            getVoucher({id: customerId, tongTien: totalPrice});
        } else {
            document.getElementById("showDanhSachVouCher").innerText = "Vui Lòng Chọn Sản phẩm";
        }

        totalPrices.innerText = totalPrice.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
        tongTien = totalPrice;

        document.getElementById("btnThanToan").style.display = listCartId.length > 0 ? "block" : "none";
    }

    chonAll.addEventListener("change", () => {
        chon.forEach(item => item.checked = chonAll.checked);
        updateTotalPrice();
    });

    chon.forEach(item => {
        item.addEventListener("change", () => {
            chonAll.checked = [...chon].every(cb => cb.checked);
            updateTotalPrice();
        });
    });

    document.querySelectorAll(".inputSoLuong").forEach(item => {
        item.addEventListener("blur", function () {
            let value = Number(item.value);
            if (isNaN(value) || value < 1) {
                this.value = 1; // Nếu nhập sai, tự động sửa thành 1
            }
            let data = {id: cartId.value, quantity: this.value};
            sendServer(data);
        });
    });
});

let showVoucher = document.getElementById("showDanhSachVouCher");

function getVoucher(data) {
    fetch("/api/cart/get-voucher", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(arrVoucher => {
            console.log("Response from server:", arrVoucher);

            if (arrVoucher.length === 0) {
                showVoucher.innerText = "Không có voucher nào khả dụng!";
                return;
            }

            showVoucher.innerText = '';

            let bestVoucher = arrVoucher.reduce((max, voucher) =>
                parseFloat(voucher.giamToiDa) > parseFloat(max.giamToiDa) ? voucher : max, arrVoucher[0]);

            showVoucher.innerHTML = arrVoucher.map(item => `
            <tr>
                <td>${item.ten}</td>
                <td>${item.giamToiDa} VND</td>
                <td>
                    <input type="radio" name="chonvoucher" class="voucher-radio"
                        value="${item.id}" data-value="${item.giamToiDa}"
                        ${item.id === bestVoucher.id ? "checked" : ""}>
                </td>
            </tr>
        `).join("");

            handlerVoucher(bestVoucher.id, bestVoucher.giamToiDa);
        })
        .catch(error => console.error("Lỗi:", error));
}

function handlerVoucher(defaultVoucherId = null, defaultDiscountValue = null) {
    let selectedVoucherId = defaultVoucherId;
    let selectedDiscountValue = defaultDiscountValue;

    document.querySelectorAll(".voucher-radio").forEach(radio => {
        radio.addEventListener("change", function () {
            selectedVoucherId = this.value;
            selectedDiscountValue = this.getAttribute("data-value");
            console.log(`Voucher được chọn: ID = ${selectedVoucherId}, Giá trị giảm = ${selectedDiscountValue} VND`);
        });
    });

    document.getElementById("confirmVoucher").addEventListener("click", function () {
        if (selectedVoucherId) {
            document.getElementById("voucherId").value = selectedVoucherId;
            updateDiscount(selectedDiscountValue);
        } else {
            console.log("Vui lòng chọn một voucher!");
        }
    });

    if (defaultVoucherId && document.querySelectorAll(".chon:checked").length > 0) {
        setTimeout(() => {
            let bestVoucherInput = document.querySelector(`input[value="${defaultVoucherId}"]`);
            if (bestVoucherInput) {
                bestVoucherInput.click();
                updateDiscount(defaultDiscountValue);
                document.getElementById("voucherId").value = bestVoucherInput.value
            } else {
                console.error("Không tìm thấy voucher tốt nhất!");
            }
        }, 100);
    }
}

// Cập nhật tổng tiền sau khi áp dụng voucher
function updateDiscount(discountValue) {
    let discount = parseFloat(discountValue) || 0;
    let tongTienNew = tongTien - discount;
    document.getElementById("soTienGiam").innerText = convertToVND(discount)
    document.getElementById("showTongTien").innerText = convertToVND(tongTienNew)
    console.log("tong tiền trước khi giảm : ", totalPriceAfterDiscount)
    document.getElementById("totalPriceAfterDiscounts").innerText = convertToVND(totalPriceAfterDiscount)
}

function convertToVND(amount) {
    if (isNaN(amount)) return "0 VND"; // Kiểm tra nếu không phải số
    return amount.toLocaleString('vi-VN') + " VNĐ";
}


