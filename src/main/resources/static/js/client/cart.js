document.addEventListener("DOMContentLoaded", function () {
    const quantity = document.getElementById("inputQuantity");
    const cartId = document.getElementById("idCart");
    const customerId = document.getElementById("customerid")?.value;
    const removeAllLink = document.getElementById("removeAllLink");
    const chonAll = document.getElementById("chonall");
    const chon = document.querySelectorAll(".chon");
    const totalPrices = document.getElementById("showTongTien");
    const showVoucher = document.getElementById("showDanhSachVouCher");
    const confirmVoucherBtn = document.getElementById("confirmVoucher");

    let tongTien = 0;
    let totalPriceAfterDiscount = 0;
    let selectedVoucherId = null;
    let selectedDiscountValue = null;

    if (removeAllLink && customerId) {
        removeAllLink.href = "/cart/remove-all/" + customerId;
    }

    if (quantity) {
        quantity.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    }

    function sendServer(data) {
        fetch("/api/cart/update-quantity", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        });
        setTimeout(() => location.reload(true), 500);
    }

    function safeSetText(id, text) {
        const el = document.getElementById(id);
        if (el) el.innerText = text;
    }

    function safeSetDisplay(id, value) {
        const el = document.getElementById(id);
        if (el) el.style.display = value;
    }

    function updateTotalPrice() {

        safeSetText("soTienGiam", convertToVND(0));
        safeSetText("totalPriceAfterDiscounts", convertToVND(0));
        safeSetText("showVoucher", "Vui Lòng Chọn Sản phẩm");
        safeSetDisplay("btnThanToan", "none");
        document.getElementById("voucherId").value = 0
        document.getElementById("showTongTien").innerText = 0;

        console.log("vào hàm update price ()*********")
        tongTien = 0;
        totalPriceAfterDiscount = 0;
        selectedVoucherId = null;
        selectedDiscountValue = null;

        let selectedCartIds = [];

        chon.forEach((checkbox, index) => {
            if (checkbox.checked) {
                const priceString = document.querySelectorAll(".tongtien")[index]?.textContent || "0";
                console.log("tong tien cua san pham : ", priceString)
                document.getElementById("showTongTien").innerText = priceString
                const priceFloat = parseFloat(priceString.replace(/,/g, '').replace(/[^\d.]/g, '')) || 0;
                tongTien += priceFloat;
                totalPriceAfterDiscount += priceFloat;

                const currentCartId = document.querySelectorAll(".idGioHang")[index]?.value;
                if (currentCartId) selectedCartIds.push(currentCartId);
            }
        });

        const cartIdsInput = document.getElementById("cartIds");
        if (cartIdsInput) cartIdsInput.value = selectedCartIds.join(",");

        if (selectedCartIds.length > 0) {
            safeSetText("totalPrices", convertToVND(tongTien));
            safeSetDisplay("btnThanToan", "block");
            getVoucher({customerId, tongTien});
        } else {
            safeSetText("totalPrices", convertToVND(0));
        }
    }

    function getVoucher(data) {
        fetch("/api/cart/get-voucher", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(arrVoucher => {
                if (!arrVoucher.length) {
                    if (showVoucher) showVoucher.innerText = "Không có voucher nào khả dụng!";
                    return;
                }

                let bestVoucher = arrVoucher.reduce((max, v) =>
                        parseFloat(v.giamToiDa) > parseFloat(max.giamToiDa) ? v : max,
                    arrVoucher[0]
                );

                if (showVoucher) {
                    showVoucher.innerHTML = arrVoucher.map(v => `
                        <tr>
                            <td>${v.ten}</td>
                            <td>${convertToVND(v.giamToiDa)}</td>
                            <td>
                                <input type="radio" name="chonvoucher" class="voucher-radio"
                                    value="${v.id}" data-value="${v.giamToiDa}"
                                    ${v.id === bestVoucher.id ? "checked" : ""}>
                            </td>
                        </tr>
                    `).join("");
                }

                selectedVoucherId = bestVoucher.id;
                selectedDiscountValue = bestVoucher.giamToiDa;

                setupVoucherListeners();
                updateDiscount(selectedDiscountValue);

                const voucherInput = document.getElementById("voucherId");
                if (voucherInput) voucherInput.value = selectedVoucherId;
            })
            .catch(err => console.error("Lỗi gọi voucher:", err));
    }

    function setupVoucherListeners() {
        document.querySelectorAll(".voucher-radio").forEach(radio => {
            radio.addEventListener("change", function () {
                selectedVoucherId = this.value;
                selectedDiscountValue = this.getAttribute("data-value");
            });
        });

        if (confirmVoucherBtn) {
            confirmVoucherBtn.addEventListener("click", function () {
                if (selectedVoucherId) {
                    const voucherInput = document.getElementById("voucherId");
                    if (voucherInput) voucherInput.value = selectedVoucherId;
                    updateDiscount(selectedDiscountValue);
                } else {
                    console.log("Vui lòng chọn voucher!");
                }
            });
        }
    }

    function updateDiscount(discountValue) {
        let discount = parseFloat(discountValue) || 0;
        let tongTienSauGiam = tongTien - discount;
        if (tongTienSauGiam < 0) tongTienSauGiam = 0;

        safeSetText("soTienGiam", convertToVND(discount));
        safeSetText("showTongTien", convertToVND(tongTienSauGiam));
        safeSetText("totalPriceAfterDiscounts", convertToVND(totalPriceAfterDiscount));
    }

    function convertToVND(amount) {
        if (isNaN(amount)) return "0 VND";
        return parseFloat(amount).toLocaleString('vi-VN') + " VNĐ";
    }

    if (chonAll) {
        chonAll.addEventListener("change", () => {
            chon.forEach(item => item.checked = chonAll.checked);
            updateTotalPrice();
        });
    }

    chon.forEach(item => {
        item.checked = false;
        item.addEventListener("change", () => {
            if (chonAll) chonAll.checked = [...chon].every(cb => cb.checked);
            updateTotalPrice();
        });
    });

    document.querySelectorAll(".inputSoLuong").forEach(item => {
        item.addEventListener("blur", function () {
            // lấy thẻ cha
            console.log("item : ",item)
            console.log("id product update : ",item.id)
            let value = Number(item.value);
            if (isNaN(value) || value < 1) {
                this.value = 1;
            }
            console.log("quanity update : ", this.value)
            sendServer({id: item.id, quantity: this.value});
        });
    });

    const message = document.getElementById("messageRP")?.innerText;
    const success = document.getElementById("successRP")?.innerText;
    if (message && success) {
        Swal.fire({
            position: "top-end",
            icon: success.trim() === "true" ? "success" : "error",
            width: '400px',
            title: message,
            showConfirmButton: false,
            timer: 2000,
            customClass: {
                title: 'small-title',
                popup: 'small-popup'
            }
        });
    }
});
