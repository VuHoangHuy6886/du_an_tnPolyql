let product = null;
let selectedColor = null; // Không chọn sẵn
let selectedSize = null;  // Không chọn sẵn
let colorIdMap = {};

// Hàm format tiền VNĐ, làm tròn đến nghìn
function formatCurrencyVND(value) {
    const rounded = Math.round(value / 1000) * 1000;
    return rounded.toLocaleString("vi-VN") + " VNĐ";
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

fetch(`http://localhost:8080/api/san-pham?id=${productId}`)
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
    minusBtn.addEventListener("click", () => {
        let currentVal = parseInt(quantityInput.value);
        if (currentVal > 1) {
            quantityInput.value = currentVal - 1;
        }
    });
    const quantityInput = document.createElement("input");
    quantityInput.type = "number";
    quantityInput.value = "1";
    quantityInput.min = "1";
    const plusBtn = document.createElement("button");
    plusBtn.type = "button";
    plusBtn.textContent = "+";
    plusBtn.addEventListener("click", () => {
        let currentVal = parseInt(quantityInput.value);
        quantityInput.value = currentVal + 1;
    });
    quantityWrapper.appendChild(minusBtn);
    quantityWrapper.appendChild(quantityInput);
    quantityWrapper.appendChild(plusBtn);
    rightCol.appendChild(quantityWrapper);

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
        // Tính giá hiệu quả: nếu có giảm giá và đợt giảm giá đang hoạt động thì dùng giá giảm, ngược lại dùng giá gốc.
        const effectivePrice = (variant.giaChietKhau && variant.giaChietKhau < variant.giaBan && isDiscountActive(variant))
            ? variant.giaChietKhau : variant.giaBan;
        const total = effectivePrice * quantity;
        if (total > 5000000) {
            Swal.fire({
                icon: 'error',
                title: 'Đơn hàng vượt quá giới hạn',
                text: 'Hãy liên hệ với chúng tôi để mua số lượng lớn với giá tốt nhất.'
            });
            return;
        }
        console.log("Thêm vào giỏ hàng:", { variant, quantity });
        fetch("http://localhost:8080/api/gio-hang", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                idSanPhamChiTiet: parseInt(variant.id),
                soLuong: parseInt(quantity),
                idKhachHang:1
            }),
        })
            .then(response => {
                console.log("Response status:", response.status);
                return response.json();
            })
            .then(data => {
                console.log("Phản hồi từ API:", data);

                // Kiểm tra nếu API trả về một đối tượng có ID (hoặc dữ liệu hợp lệ khác)
                if (data.id) {
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
        if (descriptionContainer.scrollHeight > 200) {
            descriptionContainer.style.maxHeight = "200px";
            descriptionContainer.style.overflow = "hidden";
            seeMoreBtn.style.display = "block";
        }
    }, 100);
    row.appendChild(descriptionWrapper);

    container.appendChild(row);

    updateImages();
    updateMainPrice();
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

// Cập nhật hiển thị giá
function updateMainPrice() {
    const priceSection = document.getElementById("price-section");
    const variantInfoDiv = document.getElementById("variant-info");

    if (!selectedColor || !selectedSize) {
        let minPrice = Infinity;
        let maxPrice = 0;
        product.sanPhamChiTiets.forEach(v => {
            const base = (v.giaChietKhau && v.giaChietKhau < v.giaBan) ? v.giaChietKhau : v.giaBan;
            if (base < minPrice) minPrice = base;
            if (base > maxPrice) maxPrice = base;
        });
        priceSection.innerHTML = `Giá: ${formatCurrencyVND(minPrice)} - ${formatCurrencyVND(maxPrice)}`;
        variantInfoDiv.innerHTML = "";
        return;
    }

    const variant = product.sanPhamChiTiets.find(
        v => v.mauSac.ten === selectedColor && v.kichThuoc.ten === selectedSize
    );
    if (!variant) {
        priceSection.innerHTML = `<p class="text-danger">Biến thể không tồn tại</p>`;
        variantInfoDiv.innerHTML = "";
        return;
    }

    const basePrice = variant.giaBan;
    const discountPrice = variant.giaChietKhau;
    if (discountPrice && discountPrice < basePrice && isDiscountActive(variant)) {
        const discountPercent = Math.round(((basePrice - discountPrice) / basePrice) * 100);
        priceSection.innerHTML = `
          <span class="old-price">Giá cũ: ${formatCurrencyVND(basePrice)}</span>
          <span class="main-price">Giá: ${formatCurrencyVND(discountPrice)}</span>
          <span class="discount-tag">- ${discountPercent}%</span>
          <div class="discount-info">Tiết kiệm: ${formatCurrencyVND(basePrice - discountPrice)}</div>
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