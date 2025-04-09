let product = null;
let selectedColor = null; // Không chọn sẵn
let selectedSize = null;  // Không chọn sẵn
let colorIdMap = {};

// Hàm format tiền VNĐ, làm tròn đến nghìn
function formatCurrencyVND(value) {
    const rounded = Math.round(value / 1000) * 1000;
    return rounded.toLocaleString("vi-VN") + " VNĐ";
}

// Hàm lấy giá hiệu quả của biến thể (dùng chung cho hiển thị giá chính và tính tổng)
function getEffectivePrice(variant) {
    let effectivePrice = variant.giaBan;
    if (variant.isPromotionProduct && variant.dotGiamGia) {
        if (variant.dotGiamGia.loaiChietKhau === 'PHAN_TRAM') {
            effectivePrice = variant.giaChietKhau;
        } else {
            effectivePrice = variant.giaBan - variant.dotGiamGia.giaTriGiam;
        }
    }
    return effectivePrice;
}

// Hàm kiểm tra đợt giảm giá có hoạt động (giả sử đối tượng dotGiamGia có isActive, startDate, endDate)
function isDiscountActive(variant) {
    if (variant.dotGiamGia && variant.dotGiamGia.isActive) {
        const now = new Date();
        const start = new Date(variant.dotGiamGia.startDate);
        const end = new Date(variant.dotGiamGia.endDate);
        return now >= start && now <= end;
    }
    return false;
}

// Lấy id sản phẩm từ URL (ví dụ: /san-pham/5)
const path = window.location.pathname;
const productId = path.split('/').pop() || '1';

fetch(`/api/san-pham?id=${productId}`)
    .then(response => response.json())
    .then(data => {
        document.getElementById("loading").style.display = "none";
        product = data.content[0];
        renderBreadcrumb(product);
        renderProductDetail(product);
    })
    .catch(error => {
        console.error("Lỗi lấy dữ liệu:", error);
        document.getElementById("loading").innerHTML =
            '<p class="text-danger">Không thể tải dữ liệu sản phẩm.</p>';
    });

function renderBreadcrumb(prod) {
    const breadcrumbArea = document.getElementById("breadcrumb-area");
    let html = `<a href="#">Trang chủ</a>`;
    if (prod.danhMucs && prod.danhMucs.length > 0) {
        const mainCategory = prod.danhMucs[0].ten;
        html += ` / <a href="#">${mainCategory}</a>`;
    }
    breadcrumbArea.innerHTML = html;
}

function renderProductDetail(prod) {
    const container = document.getElementById("product-container");
    container.innerHTML = "";

    const row = document.createElement("div");
    row.className = "row product-detail-card";

    // Cột trái: Carousel ảnh
    const leftCol = document.createElement("div");
    leftCol.className = "col-lg-6 col-md-12 p-4";
    leftCol.id = "left-col-images";
    row.appendChild(leftCol);

    // Cột phải: Thông tin sản phẩm
    const rightCol = document.createElement("div");
    rightCol.className = "col-lg-6 col-md-12 product-info";

    // Tên sản phẩm
    const titleEl = document.createElement("h2");
    titleEl.className = "product-title";
    titleEl.textContent = prod.ten;
    rightCol.appendChild(titleEl);

    // Rating
    const ratingEl = document.createElement("div");
    ratingEl.innerHTML = `
        <span class="product-rating">
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star"></i>
          <i class="fa-solid fa-star-half-stroke"></i>
          4.8
        </span>
        <span class="text-muted ms-3">Đã bán 14,7k</span>
      `;
    rightCol.appendChild(ratingEl);

    // Khung giá
    const priceSection = document.createElement("div");
    priceSection.className = "price-section";
    priceSection.id = "price-section";
    rightCol.appendChild(priceSection);

    // Hãng, Kiểu dáng, Chất liệu
    const attributeDiv = document.createElement("div");
    attributeDiv.className = "product-attributes";
    attributeDiv.innerHTML = `
        <p><strong>Hãng:</strong> ${prod.thuongHieu?.ten || "N/A"}</p>
        <p><strong>Kiểu dáng:</strong> ${prod.kieuDang?.ten || "N/A"}</p>
        <p><strong>Chất liệu:</strong> ${prod.chatLieu?.ten || "N/A"}</p>
      `;
    rightCol.appendChild(attributeDiv);

    // Màu sắc
    const colorLabel = document.createElement("div");
    colorLabel.className = "variant-label";
    colorLabel.textContent = "Màu sắc:";
    rightCol.appendChild(colorLabel);

    const colorOptions = document.createElement("div");
    colorOptions.className = "color-options";
    prod.sanPhamChiTiets.forEach(ct => {
        colorIdMap[ct.mauSac.ten] = ct.mauSac.id;
    });
    const uniqueColors = [...new Set(prod.sanPhamChiTiets.map(ct => ct.mauSac.ten))];
    uniqueColors.forEach((colorName) => {
        const swatch = document.createElement("div");
        swatch.className = "color-swatch";
        swatch.textContent = colorName;
        swatch.addEventListener("click", () => {
            if (swatch.classList.contains("active")) {
                swatch.classList.remove("active");
                selectedColor = null;
            } else {
                document.querySelectorAll(".color-swatch").forEach(el => el.classList.remove("active"));
                swatch.classList.add("active");
                selectedColor = colorName;
            }
            updateDependentOptions();
            updateImages();
            updateMainPrice();
            updateTotalPrice();
        });
        colorOptions.appendChild(swatch);
    });
    rightCol.appendChild(colorOptions);

    // Kích thước
    const sizeLabel = document.createElement("div");
    sizeLabel.className = "variant-label";
    sizeLabel.textContent = "Kích thước:";
    rightCol.appendChild(sizeLabel);

    const sizeOptions = document.createElement("div");
    sizeOptions.className = "size-options";
    const uniqueSizes = [...new Set(prod.sanPhamChiTiets.map(ct => ct.kichThuoc.ten))];
    uniqueSizes.forEach((sizeName) => {
        const swatch = document.createElement("div");
        swatch.className = "size-swatch";
        swatch.textContent = sizeName;
        swatch.addEventListener("click", () => {
            if (swatch.classList.contains("active")) {
                swatch.classList.remove("active");
                selectedSize = null;
            } else {
                document.querySelectorAll(".size-swatch").forEach(el => el.classList.remove("active"));
                swatch.classList.add("active");
                selectedSize = sizeName;
            }
            updateDependentOptions();
            updateMainPrice();
            updateTotalPrice();
        });
        sizeOptions.appendChild(swatch);
    });
    rightCol.appendChild(sizeOptions);

    // Số lượng
    const quantityWrapper = document.createElement("div");
    quantityWrapper.className = "quantity-wrapper";
    const minusBtn = document.createElement("button");
    minusBtn.type = "button";
    minusBtn.textContent = "-";
    const quantityInput = document.createElement("input");
    quantityInput.type = "number";
    quantityInput.value = "1";
    quantityInput.min = "1";
    const plusBtn = document.createElement("button");
    plusBtn.type = "button";
    plusBtn.textContent = "+";
    quantityWrapper.appendChild(minusBtn);
    quantityWrapper.appendChild(quantityInput);
    quantityWrapper.appendChild(plusBtn);
    rightCol.appendChild(quantityWrapper);

    // Phần hiển thị tổng giá và cảnh báo (được chèn ngay dưới quantity-wrapper)
    const totalPriceMsgEl = document.createElement("div");
    totalPriceMsgEl.id = "total-price-message";
    totalPriceMsgEl.style.marginTop = "10px";
    rightCol.appendChild(totalPriceMsgEl);

    // Sự kiện cập nhật số lượng (cả qua button và thay đổi trực tiếp)
    minusBtn.addEventListener("click", () => {
        let currentVal = parseInt(quantityInput.value);
        if (currentVal > 1) {
            quantityInput.value = currentVal - 1;
            updateTotalPrice();
        }
    });
    plusBtn.addEventListener("click", () => {
        let currentVal = parseInt(quantityInput.value);
        quantityInput.value = currentVal + 1;
        updateTotalPrice();
    });
    quantityInput.addEventListener("change", updateTotalPrice);

    // Hàm cập nhật hiển thị tổng tiền dựa trên số lượng và biến thể đã chọn
    function updateTotalPrice() {
        if (!selectedColor || !selectedSize) {
            totalPriceMsgEl.innerHTML = "";
            return;
        }
        const variant = prod.sanPhamChiTiets.find(
            v => v.mauSac.ten === selectedColor && v.kichThuoc.ten === selectedSize
        );
        if (!variant) {
            totalPriceMsgEl.innerHTML = "";
            return;
        }
        const effectivePrice = getEffectivePrice(variant);
        let quantity = parseInt(quantityInput.value);

        // Kiểm tra nếu số lượng <= 0 thì hiển thị thông báo lỗi
        if (quantity <= 0) {
            totalPriceMsgEl.innerHTML = `<div class="text-danger">Số lượng không hợp lệ</div>`;
            return;
        }

        const total = effectivePrice * quantity;
        let html = `<div><strong>Tổng:</strong> ${formatCurrencyVND(total)}</div>`;
        if (total > 5000000) {
            html += `<div class="text-danger">Số tiền tối đa là 5 triệu, vui lòng đặt hàng lại hoặc liên hệ với chúng tôi!</div>`;
        }
        totalPriceMsgEl.innerHTML = html;
    }


    // Action Buttons
    const actionButtons = document.createElement("div");
    actionButtons.className = "action-buttons";
    const cartBtn = document.createElement("button");
    cartBtn.className = "btn-cart";
    cartBtn.textContent = "Thêm vào giỏ hàng";
    cartBtn.addEventListener("click", () => {
        if (!selectedColor || !selectedSize) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi chọn sản phẩm',
                text: 'Vui lòng chọn màu và kích thước hợp lệ.'
            });
            return;
        }
        const variant = prod.sanPhamChiTiets.find(
            v => v.mauSac.ten === selectedColor && v.kichThuoc.ten === selectedSize
        );
        let quantity = parseInt(quantityInput.value);
        if (isNaN(quantity) || quantity < 1) {
            Swal.fire({
                icon: 'error',
                title: 'Số lượng không hợp lệ',
                text: 'Vui lòng nhập số lượng nguyên dương (ít nhất 1).'
            });
            return;
        }
        // Kiểm tra nếu số lượng đặt hàng vượt quá số lượng có sẵn
        if (quantity > variant.soLuong) {
            Swal.fire({
                icon: 'error',
                title: 'Số lượng không đủ',
                text: `Số lượng có sẵn chỉ còn ${variant.soLuong.toLocaleString()} sản phẩm.`
            });
            return;
        }

        // Tính giá hiệu quả: nếu có giảm giá và đợt giảm giá đang hoạt động thì dùng giá giảm, ngược lại dùng giá gốc.
        let effectivePrice = getEffectivePrice(variant);
        const total = effectivePrice * quantity;
        if (total > 5000000) {
            Swal.fire({
                icon: 'error',
                title: 'Vui lòng đặt đơn hàng dưới 5 triệu',
                text: `Số tiền hiện tại của bạn là: ${total.toLocaleString('vi-VN')} VND. Hãy liên hệ với chúng tôi để mua số lượng lớn với giá tốt nhất.`
            });
            return;
        }
        console.log("Thêm vào giỏ hàng:", { variant, quantity });
        fetch("/api/gio-hang", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                idSanPhamChiTiet: parseInt(variant.id),
                soLuong: parseInt(quantity),
            }),
        })
            .then(response => {
                console.log("Response status:", response.status);
                return response.json();
            })
            .then(data => {
                console.log("Phản hồi từ API:", data);
                if (data.id) {
                    // Giảm số lượng sản phẩm hiển thị trên giao diện:
                    // Sau khi thêm vào giỏ hàng thành công, trừ số lượng có sẵn của biến thể.
                    variant.soLuong = variant.soLuong - quantity;
                    updateMainPrice();

                    Swal.fire({
                        icon: "success",
                        title: "Thành công!",
                        text: "Sản phẩm đã được thêm vào giỏ hàng.",
                        showConfirmButton: false,
                        timer: 1500,
                    });
                    $("#addCartModal").modal("hide");
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Lỗi",
                        text: "Không thể thêm sản phẩm vào giỏ hàng.",
                    });
                }
            })
            .catch(error => {
                console.error("Lỗi API giỏ hàng:", error);
                Swal.fire({
                    icon: "error",
                    title: "Lỗi hệ thống",
                    text: "Vui lòng thử lại sau.",
                });
            });
    });

    const buyBtn = document.createElement("button");
    buyBtn.className = "btn-buy";
    buyBtn.textContent = "Mua ngay";
    buyBtn.addEventListener("click", () => {
        Swal.fire({
            icon: 'info',
            title: 'Thanh toán',
            text: 'Chuyển sang trang thanh toán (demo).'
        });
    });
    actionButtons.appendChild(cartBtn);
    actionButtons.appendChild(buyBtn);
    rightCol.appendChild(actionButtons);

    // Shipping / Policy
    const shippingDiv = document.createElement("div");
    shippingDiv.className = "shipping-info";
    shippingDiv.innerHTML = `
        <p><i class="fa-solid fa-arrows-rotate"></i> Đổi trả miễn phí trong 60 ngày</p>
        <p><i class="fa-solid fa-headset"></i> Hỗ trợ 24/7</p>
      `;
    rightCol.appendChild(shippingDiv);

    // Thông tin biến thể
    const variantInfoDiv = document.createElement("div");
    variantInfoDiv.className = "mt-2";
    variantInfoDiv.id = "variant-info";
    rightCol.appendChild(variantInfoDiv);

    row.appendChild(rightCol);

    // Phần mô tả sản phẩm với nút "Xem thêm/Ẩn bớt"
    const descriptionWrapper = document.createElement("div");
    descriptionWrapper.className = "p-4";
    const descriptionContainer = document.createElement("div");
    descriptionContainer.id = "product-description";
    descriptionContainer.innerHTML = prod.moTa;
    descriptionContainer.classList.add("collapsed");
    descriptionWrapper.appendChild(descriptionContainer);
    const seeMoreBtn = document.createElement("div");
    seeMoreBtn.className = "see-more-btn";
    seeMoreBtn.innerHTML = `<button type="button" class="btn btn-link">Xem thêm</button>`;
    seeMoreBtn.style.display = "none";
    seeMoreBtn.addEventListener("click", () => {
        if (descriptionContainer.classList.contains("expanded")) {
            descriptionContainer.classList.remove("expanded");
            descriptionContainer.classList.add("collapsed");
            descriptionContainer.style.maxHeight = "500px";
            seeMoreBtn.innerHTML = `<button type="button" class="btn btn-link">Xem thêm</button>`;
        } else {
            descriptionContainer.classList.remove("collapsed");
            descriptionContainer.classList.add("expanded");
            descriptionContainer.style.maxHeight = "none";
            seeMoreBtn.innerHTML = `<button type="button" class="btn btn-link">Ẩn bớt</button>`;
        }
    });
    descriptionWrapper.appendChild(seeMoreBtn);
    setTimeout(() => {
        if (descriptionContainer.scrollHeight > 500) {
            descriptionContainer.style.maxHeight = "500px";
            descriptionContainer.style.overflow = "hidden";
            seeMoreBtn.style.display = "block";
        }
    }, 100);
    row.appendChild(descriptionWrapper);

    container.appendChild(row);

    updateImages();
    updateMainPrice();
    updateTotalPrice();
    updateDependentOptions();
}

// Cập nhật ảnh sử dụng Bootstrap Carousel (không tự động chuyển)
function updateImages() {
    const leftCol = document.getElementById("left-col-images");
    leftCol.innerHTML = "";
    const images = product.anhs && product.anhs.length > 0 ? product.anhs : [{ url: product.anhUrl }];
    leftCol.innerHTML = buildCarouselHtml(images);

    if (selectedColor) {
        const cId = Number(colorIdMap[selectedColor]);
        const matchingIndex = images.findIndex(img => Number(img.mauSacId) === cId);
        if (matchingIndex !== -1) {
            let carouselEl = document.getElementById("carouselProduct");
            if (carouselEl) {
                let carousel = bootstrap.Carousel.getInstance(carouselEl);
                if (!carousel) {
                    carousel = new bootstrap.Carousel(carouselEl, { interval: false, touch: true });
                }
                carousel.to(matchingIndex);
            }
        }
    }
}

function buildCarouselHtml(images) {
    if (!images || images.length === 0) return "";
    let indicatorsHtml = "";
    let itemsHtml = "";
    images.forEach((img, index) => {
        indicatorsHtml += `<button type="button" data-bs-target="#carouselProduct" data-bs-slide-to="${index}" ${index === 0 ? 'class="active" aria-current="true"' : ""} aria-label="Slide ${index + 1}"></button>`;
        itemsHtml += `<div class="carousel-item ${index === 0 ? "active" : ""}">
                        <img src="${img.url}" class="d-block w-100" alt="product">
                      </div>`;
    });
    return `<div id="carouselProduct" class="carousel slide" data-bs-touch="true" data-bs-interval="false">
                <div class="carousel-indicators">
                  ${indicatorsHtml}
                </div>
                <div class="carousel-inner">
                  ${itemsHtml}
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#carouselProduct" data-bs-slide="prev">
                  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#carouselProduct" data-bs-slide="next">
                  <span class="carousel-control-next-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Next</span>
                </button>
              </div>`;
}

// Cập nhật hiển thị giá chính và thông tin biến thể
function updateMainPrice() {
    const priceSection = document.getElementById("price-section");
    const variantInfoDiv = document.getElementById("variant-info");
    // Khi chưa chọn biến thể, hiển thị khoảng giá dựa trên giá hiệu quả của từng sản phẩm chi tiết
    if (!selectedColor || !selectedSize) {
        let minPrice = Infinity;
        let maxPrice = 0;
        product.sanPhamChiTiets.forEach(v => {
            let effectivePrice = getEffectivePrice(v);
            if (effectivePrice < minPrice) minPrice = effectivePrice;
            if (effectivePrice > maxPrice) maxPrice = effectivePrice;
        });
        priceSection.innerHTML = `Giá: ${formatCurrencyVND(minPrice)} - ${formatCurrencyVND(maxPrice)}`;
        variantInfoDiv.innerHTML = "";
        return;
    }

    // Tìm biến thể dựa trên màu sắc và kích thước đã chọn
    const variant = product.sanPhamChiTiets.find(
        v => v.mauSac.ten === selectedColor && v.kichThuoc.ten === selectedSize
    );
    if (!variant) {
        priceSection.innerHTML = `<p class="text-danger">Biến thể không tồn tại</p>`;
        variantInfoDiv.innerHTML = "";
        return;
    }
    if (variant.soLuong <= 0) {
        priceSection.innerHTML = `<p class="text-danger">Sa phẩm hết hàng</p>`;
        variantInfoDiv.innerHTML = "";
        return;
    }

    const basePrice = variant.giaBan;
    let effectivePrice = getEffectivePrice(variant);
    let discountPercent = 0;

    if (effectivePrice < basePrice) {
        discountPercent = Math.round(((basePrice - effectivePrice) / basePrice) * 100);
        priceSection.innerHTML = `
          <span class="main-price">Giá: ${formatCurrencyVND(effectivePrice)}</span>
          <span class="old-price">Giá cũ: ${formatCurrencyVND(basePrice)}</span>
          <span class="discount-tag">- ${discountPercent}%</span>
          <div class="discount-info">Tiết kiệm: ${formatCurrencyVND(basePrice - effectivePrice)}</div>
        `;
    } else {
        priceSection.innerHTML = `Giá: ${formatCurrencyVND(basePrice)}`;
    }
    variantInfoDiv.innerHTML = `
        <div class="variant-info mt-2">
          <p><strong>Số lượng có sẵn:</strong> ${variant.soLuong.toLocaleString()}</p>
        </div>
      `;
}

// Cập nhật các tùy chọn phụ thuộc giữa màu và kích thước
function updateDependentOptions() {
    if (selectedColor) {
        const availableSizes = new Set(product.sanPhamChiTiets
            .filter(v => v.mauSac.ten === selectedColor)
            .map(v => v.kichThuoc.ten));
        document.querySelectorAll('.size-swatch').forEach(swatch => {
            const sizeName = swatch.textContent.trim();
            if (availableSizes.has(sizeName)) {
                swatch.classList.remove('disabled-option');
                swatch.style.pointerEvents = 'auto';
                swatch.style.opacity = '1';
            } else {
                swatch.classList.remove('active');
                swatch.classList.add('disabled-option');
                swatch.style.pointerEvents = 'none';
                swatch.style.opacity = '0.5';
                if (selectedSize === sizeName) selectedSize = null;
            }
        });
    } else {
        document.querySelectorAll('.size-swatch').forEach(swatch => {
            swatch.classList.remove('disabled-option');
            swatch.style.pointerEvents = 'auto';
            swatch.style.opacity = '1';
        });
    }
    if (selectedSize) {
        const availableColors = new Set(product.sanPhamChiTiets
            .filter(v => v.kichThuoc.ten === selectedSize)
            .map(v => v.mauSac.ten));
        document.querySelectorAll('.color-swatch').forEach(swatch => {
            const colorName = swatch.textContent.trim();
            if (availableColors.has(colorName)) {
                swatch.classList.remove('disabled-option');
                swatch.style.pointerEvents = 'auto';
                swatch.style.opacity = '1';
            } else {
                swatch.classList.remove('active');
                swatch.classList.add('disabled-option');
                swatch.style.pointerEvents = 'none';
                swatch.style.opacity = '0.5';
                if (selectedColor === colorName) selectedColor = null;
            }
        });
    } else {
        if (!selectedColor) {
            document.querySelectorAll('.color-swatch').forEach(swatch => {
                swatch.classList.remove('disabled-option');
                swatch.style.pointerEvents = 'auto';
                swatch.style.opacity = '1';
            });
        }
    }
}
