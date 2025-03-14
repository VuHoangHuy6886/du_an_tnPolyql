/***************** Global Variables *****************/
var selectedColor = null;
var selectedSize = null;
var currentProduct = null;
var lastQuery = {};

/***************** Load Filter Options *****************/
function loadFilterOptions() {
    // Dữ liệu mô phỏng – thay bằng AJAX nếu có API thật
    var thuongHieuData = [
        { id: 1, ten: "Adidas" },
        { id: 2, ten: "Nike" }
    ];
    var danhMucData = [
        { id: 1, ten: "Áo" },
        { id: 2, ten: "Quần" },
        { id: 3, ten: "Váy" }
    ];
    var chatLieuData = [
        { id: 1, ten: "Cotton" },
        { id: 2, ten: "Jean" }
    ];
    var kieuDangData = [
        { id: 1, ten: "Áo thun" },
        { id: 2, ten: "Quần jean" },
        { id: 3, ten: "Váy hoa" }
    ];
    var mauSacData = [
        { id: 1, ten: "Đỏ" },
        { id: 2, ten: "Xanh" },
        { id: 3, ten: "Đen" },
        { id: 4, ten: "Trắng" }
    ];

    // Thêm tùy chọn mặc định cho các dropdown đơn
    $("#thuongHieuSelect").append('<option value="">Tất cả</option>');
    $("#chatLieuSelect").append('<option value="">Tất cả</option>');
    $("#kieuDangSelect").append('<option value="">Tất cả</option>');
    $("#mauSacSelect").append('<option value="">Tất cả</option>');

    // Cho Danh Mục dạng multi-select
    $("#danhMucSelect").append('<option value="">Tất cả</option>');

    thuongHieuData.forEach(item => {
        $("#thuongHieuSelect").append(`<option value="${item.id}">${item.ten}</option>`);
    });
    danhMucData.forEach(item => {
        $("#danhMucSelect").append(`<option value="${item.id}">${item.ten}</option>`);
    });
    chatLieuData.forEach(item => {
        $("#chatLieuSelect").append(`<option value="${item.id}">${item.ten}</option>`);
    });
    kieuDangData.forEach(item => {
        $("#kieuDangSelect").append(`<option value="${item.id}">${item.ten}</option>`);
    });
    mauSacData.forEach(item => {
        $("#mauSacSelect").append(`<option value="${item.id}">${item.ten}</option>`);
    });
}

/***************** Get Filter Parameters *****************/
function getFilterParams() {
    var params = {};
    var thuongHieu = $("#thuongHieuSelect").val();
    if (thuongHieu) params.thuongHieuId = thuongHieu;
    var danhMuc = $("#danhMucSelect").val();
    // Nếu có chọn nhiều danh mục và không chỉ chọn "Tất cả"
    if (danhMuc && danhMuc.length > 0 && !(danhMuc.length === 1 && danhMuc[0] === "")) {
        params.danhMucIds = danhMuc;
    }
    var chatLieu = $("#chatLieuSelect").val();
    if (chatLieu) params.chatLieuId = chatLieu;
    var kieuDang = $("#kieuDangSelect").val();
    if (kieuDang) params.kieuDangId = kieuDang;
    var mauSac = $("#mauSacSelect").val();
    if (mauSac) params.mauSacId = mauSac;
    return params;
}

/***************** Render Products *****************/
function renderProducts(data) {
    $("#product-container").empty();
    var products = data.content;
    if (products.length === 0) {
        $("#product-container").html('<div class="alert alert-info">Không tìm thấy sản phẩm phù hợp!</div>');
        $("#pagination-container").empty();
        return;
    }
    products.forEach(function(product) {
        var prices = product.sanPhamChiTiets.map(detail => detail.giaBan);
        var minPrice = Math.min.apply(null, prices);
        var maxPrice = Math.max.apply(null, prices);
        var defaultImage = product.anhUrl;
        var hoverImage = (product.anhs && product.anhs.length >= 2) ? product.anhs[1].url : defaultImage;

        var card = $(`
          <div class="col-md-3 product-card mb-3">
            <div class="card h-100 shadow-sm">
              <a href="http://localhost:8080/san-pham/${product.id}">
                <img src="${defaultImage}" class="card-img-top product-img" alt="${product.ten}" data-default="${defaultImage}" data-hover="${hoverImage}">
              </a>
              <div class="card-body d-flex flex-column">
                <h5 class="card-title">
                  <a href="http://localhost:8080/san-pham/${product.id}" class="text-decoration-none text-dark">${product.ten}</a>
                </h5>
                <p class="card-text"><span>Giá: </span> ${minPrice.toLocaleString()} - ${maxPrice.toLocaleString()} VNĐ</p>
                <p class="card-text"><i class="fas fa-boxes"></i> Tồn: ${product.soLuong.toLocaleString()}</p>
                <button class="btn btn-primary mt-auto add-to-cart-btn" data-product='${JSON.stringify(product)}'>
                  <i class="fas fa-shopping-cart"></i> Thêm vào giỏ hàng
                </button>
              </div>
            </div>
          </div>
        `);
        card.find('.product-img').hover(function() {
            $(this).attr('src', $(this).data('hover'));
        }, function() {
            $(this).attr('src', $(this).data('default'));
        });
        $("#product-container").append(card);
    });
    if (data.totalPages > 1) {
        renderPagination(data.totalPages, data.number);
    } else {
        $("#pagination-container").empty();
    }
}

/***************** Fetch and Render Products *****************/
function fetchProducts(queryParams) {
    queryParams.page = (typeof queryParams.page !== "undefined") ? queryParams.page : 0;
    queryParams.pageSize = 10;
    lastQuery = queryParams;

    $("#product-container").html('<div class="text-center my-3"><i class="fas fa-spinner fa-spin fa-2x"></i> Đang tải sản phẩm...</div>');

    $.ajax({
        url: 'http://localhost:8080/api/san-pham',
        type: 'GET',
        data: queryParams,
        dataType: 'json',
        success: function(data) {
            renderProducts(data);
        },
        error: function(err) {
            console.error("Lỗi khi gọi API:", err);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Không thể tải dữ liệu sản phẩm.'
            });
        }
    });
}

/***************** Render Pagination *****************/
function renderPagination(totalPages, currentPage) {
    var paginationHTML = '';
    paginationHTML += `<li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                           <a class="page-link" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                             <span aria-hidden="true"><i class="fas fa-angle-left"></i></span>
                           </a>
                         </li>`;
    for (var i = 0; i < totalPages; i++) {
        paginationHTML += `<li class="page-item ${i == currentPage ? 'active' : ''}">
                             <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                           </li>`;
    }
    paginationHTML += `<li class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                           <a class="page-link" href="#" data-page="${currentPage + 1}" aria-label="Next">
                             <span aria-hidden="true"><i class="fas fa-angle-right"></i></span>
                           </a>
                         </li>`;
    $("#pagination-container").html(paginationHTML);
}

$(document).on("click", "#pagination-container a", function(e) {
    e.preventDefault();
    var page = $(this).data("page");
    if (page >= 0) {
        lastQuery.page = page;
        fetchProducts(lastQuery);
    }
});

/***************** Modal: Add to Cart with Dependent Options and Carousel *****************/
function openAddToCartModal(product) {
    currentProduct = product;
    $("#modal-product-name").text(product.ten);
    $("#modal-quantity").val(1);
    // Reset lựa chọn biến thể
    selectedColor = null;
    selectedSize = null;

    // Build Carousel từ product.anhs (nếu có) hoặc dùng ảnh mặc định
    var carouselInner = $("#modal-carousel-inner");
    carouselInner.empty();
    if (product.anhs && product.anhs.length > 0) {
        product.anhs.forEach(function(imgObj, index) {
            var activeClass = index === 0 ? " active" : "";
            var slide = `<div class="carousel-item${activeClass}" data-color="${imgObj.mauSacId}">
                          <img src="${imgObj.url}" class="d-block w-100" alt="${product.ten}" style="max-height:300px; object-fit:contain;">
                        </div>`;
            carouselInner.append(slide);
        });
    } else {
        carouselInner.append(`<div class="carousel-item active">
                                <img src="${product.anhUrl}" class="d-block w-100" alt="${product.ten}" style="max-height:300px; object-fit:contain;">
                              </div>`);
    }
    $("#modal-carousel").carousel(0);

    // Render lựa chọn biến thể (Màu sắc & Kích thước)
    renderVariantOptions(product);

    // Reset số lượng, giá biến thể và tổng tiền
    $("#modal-quantity").val(1);
    $("#modal-price").text("N/A");
    $("#modal-total").text("Tổng tiền: N/A");
    $("#modal-total-warning").text("");

    $("#addCartModal").modal("show");
}

// Hàm render lựa chọn biến thể theo DOM
function renderVariantOptions(prod) {
    var container = document.getElementById("modal-variant-container");
    container.innerHTML = ""; // Xóa nội dung cũ

    // Tạo phần Màu sắc
    var colorLabel = document.createElement("div");
    colorLabel.className = "variant-label mb-1";
    colorLabel.textContent = "Màu sắc:";
    container.appendChild(colorLabel);

    var colorOptions = document.createElement("div");
    colorOptions.className = "color-options d-flex flex-wrap";
    var uniqueColors = [...new Set(prod.sanPhamChiTiets.map(ct => ct.mauSac.ten))];
    uniqueColors.forEach((colorName) => {
        var swatch = document.createElement("div");
        swatch.className = "color-swatch badge badge-pill badge-light mr-2 mb-2";
        swatch.style.cursor = "pointer";
        swatch.style.border = "1px solid #ccc";
        swatch.style.padding = "5px 10px";
        swatch.textContent = colorName;
        swatch.addEventListener("click", function() {
            if (swatch.classList.contains("active")) {
                swatch.classList.remove("active");
                selectedColor = null;
            } else {
                document.querySelectorAll(".color-swatch").forEach(el => el.classList.remove("active"));
                swatch.classList.add("active");
                selectedColor = colorName;
            }
            updateDependentOptions(prod);
            updateImages(prod);
            updateMainPrice(prod);
            updateTotalAmount(prod);
        });
        colorOptions.appendChild(swatch);
    });
    container.appendChild(colorOptions);

    // Tạo phần Kích thước
    var sizeLabel = document.createElement("div");
    sizeLabel.className = "variant-label mt-3 mb-1";
    sizeLabel.textContent = "Kích thước:";
    container.appendChild(sizeLabel);

    var sizeOptions = document.createElement("div");
    sizeOptions.className = "size-options d-flex flex-wrap";
    var uniqueSizes = [...new Set(prod.sanPhamChiTiets.map(ct => ct.kichThuoc.ten))];
    uniqueSizes.forEach((sizeName) => {
        var swatch = document.createElement("div");
        swatch.className = "size-swatch badge badge-pill badge-light mr-2 mb-2";
        swatch.style.cursor = "pointer";
        swatch.style.border = "1px solid #ccc";
        swatch.style.padding = "5px 10px";
        swatch.textContent = sizeName;
        swatch.addEventListener("click", function() {
            if (swatch.classList.contains("active")) {
                swatch.classList.remove("active");
                selectedSize = null;
            } else {
                document.querySelectorAll(".size-swatch").forEach(el => el.classList.remove("active"));
                swatch.classList.add("active");
                selectedSize = sizeName;
            }
            updateDependentOptions(prod);
            updateMainPrice(prod);
            updateTotalAmount(prod);
        });
        sizeOptions.appendChild(swatch);
    });
    container.appendChild(sizeOptions);
}

// Cập nhật các tùy chọn phụ thuộc giữa màu và kích thước
function updateDependentOptions(prod) {
    var colorToSizes = {};
    var sizeToColors = {};
    prod.sanPhamChiTiets.forEach(function(ct) {
        var color = ct.mauSac.ten;
        var size = ct.kichThuoc.ten;
        if (!colorToSizes[color]) {
            colorToSizes[color] = new Set();
        }
        colorToSizes[color].add(size);
        if (!sizeToColors[size]) {
            sizeToColors[size] = new Set();
        }
        sizeToColors[size].add(color);
    });

    // Cập nhật trạng thái cho size swatches dựa trên màu đã chọn
    document.querySelectorAll(".size-swatch").forEach(function(swatch) {
        var sizeName = swatch.textContent;
        if (selectedColor) {
            if (!colorToSizes[selectedColor] || !colorToSizes[selectedColor].has(sizeName)) {
                swatch.style.opacity = "0.5";
                swatch.style.pointerEvents = "none";
                swatch.classList.remove("active");
                if (selectedSize === sizeName) selectedSize = null;
            } else {
                swatch.style.opacity = "1";
                swatch.style.pointerEvents = "auto";
            }
        } else {
            swatch.style.opacity = "1";
            swatch.style.pointerEvents = "auto";
        }
    });

    // Cập nhật trạng thái cho color swatches dựa trên kích thước đã chọn
    document.querySelectorAll(".color-swatch").forEach(function(swatch) {
        var colorName = swatch.textContent;
        if (selectedSize) {
            if (!sizeToColors[selectedSize] || !sizeToColors[selectedSize].has(colorName)) {
                swatch.style.opacity = "0.5";
                swatch.style.pointerEvents = "none";
                swatch.classList.remove("active");
                if (selectedColor === colorName) selectedColor = null;
            } else {
                swatch.style.opacity = "1";
                swatch.style.pointerEvents = "auto";
            }
        } else {
            swatch.style.opacity = "1";
            swatch.style.pointerEvents = "auto";
        }
    });
}

// Cập nhật hình ảnh carousel theo màu đã chọn
function updateImages(prod) {
    if (selectedColor) {
        var targetSlide = document.querySelector(`#modal-carousel-inner .carousel-item[data-color='${selectedColor}']`);
        if (targetSlide) {
            var index = Array.from(document.querySelectorAll("#modal-carousel-inner .carousel-item")).indexOf(targetSlide);
            $("#modal-carousel").carousel(index);
        }
    }
}

// Cập nhật giá của biến thể đã chọn
function updateMainPrice(prod) {
    if (selectedColor && selectedSize) {
        var matchingDetail = prod.sanPhamChiTiets.find(function(ct) {
            return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
        });
        if (matchingDetail) {
            $("#modal-price").text(matchingDetail.giaBan.toLocaleString() + " VNĐ");
            return;
        }
    }
    $("#modal-price").text("N/A");
}

// Cập nhật tổng tiền (giá x số lượng) và cảnh báo nếu vượt 5 triệu VNĐ
function updateTotalAmount(prod) {
    if (selectedColor && selectedSize) {
        var matchingDetail = prod.sanPhamChiTiets.find(function(ct) {
            return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
        });
        if (matchingDetail) {
            var quantity = parseInt($("#modal-quantity").val(), 10) || 0;
            var totalAmount = quantity * matchingDetail.giaBan;
            $("#modal-total").text("Tổng tiền: "+totalAmount.toLocaleString() + " VNĐ");
            if (totalAmount > 5000000) {
                $("#modal-total-warning").text("Tổng tiền vượt quá 5 triệu VNĐ!").css("color", "red");
            } else {
                $("#modal-total-warning").text("");
            }
            return;
        }
    }
    $("#modal-total").text("Tổng tiền: N/A");
    $("#modal-total-warning").text("");
}

// Lắng nghe sự thay đổi của số lượng để cập nhật tổng tiền
$("#modal-quantity").on("input", function() {
    if (currentProduct) updateTotalAmount(currentProduct);
});

/***************** Modal Form Submission *****************/
$("#modal-form").on("submit", function(e) {
    e.preventDefault();
    var quantity = parseInt($("#modal-quantity").val(), 10);
    if (isNaN(quantity) || quantity < 1) {
        Swal.fire({ icon: 'error', title: 'Lỗi!', text: 'Số lượng phải là số nguyên dương, ít nhất 1.' });
        return;
    }
    if (!selectedColor || !selectedSize) {
        Swal.fire({ icon: 'error', title: 'Chưa chọn đủ!', text: 'Vui lòng chọn cả màu sắc và kích thước.' });
        return;
    }
    var matchingDetail = currentProduct.sanPhamChiTiets.find(function(ct) {
        return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
    });
    if (!matchingDetail) {
        Swal.fire({ icon: 'error', title: 'Lỗi!', text: 'Không tìm thấy chi tiết sản phẩm phù hợp!' });
        return;
    }
    if (matchingDetail.soLuong < quantity) {
        Swal.fire({ icon: 'error', title: 'Hết hàng!', text: 'Số lượng tồn kho không đủ.' });
        return;
    }
    var totalAmount = quantity * matchingDetail.giaBan;
    if (totalAmount > 5000000) {
        Swal.fire({ icon: 'error', title: 'Giới hạn mua!', text: 'Tổng tiền mua phải dưới 5 triệu VNĐ.' });
        return;
    }
    var info = {
        sanPhamChiTietId: matchingDetail.id,
        mauSac: matchingDetail.mauSac.ten,
        kichThuoc: matchingDetail.kichThuoc.ten,
        soLuong: quantity,
        gia: matchingDetail.giaBan,
        tongTien: totalAmount
    };
    console.log("Thông tin giỏ hàng:", info);
    Swal.fire({ icon: 'success', title: 'Thành công!', text: 'Sản phẩm đã được thêm vào giỏ hàng.' });
    $("#addCartModal").modal("hide");
});

/***************** Các sự kiện khởi tạo *****************/
$(document).ready(function() {
    loadFilterOptions();
    fetchProducts({});

    $("#filter-form").on("submit", function(e) {
        e.preventDefault();
        var queryParams = getFilterParams();
        queryParams.page = 0;
        fetchProducts(queryParams);
    });

    $("#filter-form").on("reset", function() {
        setTimeout(function() {
            $("#thuongHieuSelect, #chatLieuSelect, #kieuDangSelect, #mauSacSelect").val("");
            $("#danhMucSelect").val("");
            fetchProducts({});
        }, 100);
    });

    $(document).on("click", ".add-to-cart-btn", function() {
        var productData = $(this).data("product");
        openAddToCartModal(productData);
    });
});