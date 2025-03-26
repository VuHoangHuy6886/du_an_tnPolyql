/***************** Global Variables *****************/
var selectedColor = null;
var selectedSize = null;
var currentProduct = null;
var lastQuery = {};
$(document).ready(function() {
    $('#thuongHieuSelect').select2({
        placeholder: "Chọn thương hiệu",
        allowClear: true
    });
    $('#danhMucSelect').select2({
        placeholder: "Chọn danh mục",
        allowClear: true
    });
    $('#chatLieuSelect').select2({
        placeholder: "Chọn chất liệu",
        allowClear: true
    });
    $('#kieuDangSelect').select2({
        placeholder: "Chọn kiểu dáng",
        allowClear: true
    });
    $('#mauSacSelect').select2({
        placeholder: "Chọn màu sắc",
        allowClear: true
    });
    $('#sortSelect').select2({
        placeholder: "Chọn sắp xếp",
        allowClear: true
    });
    var initialParams = getFilterParams();
    fetchProducts(initialParams);

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
            $("#sortSelect").val("");
            $("[id^='price-']").prop("checked", false);
            fetchProducts(getFilterParams());
        }, 100);
    });

    $(document).on("click", ".add-to-cart-btn", function() {
        var productData = $(this).data("product");
        openAddToCartModal(productData);
    });
});
/***************** Get Filter Parameters *****************/
function getFilterParams() {
    var params = {};

    // Thương hiệu
    var thuongHieu = $("#thuongHieuSelect").val();
    if (thuongHieu && thuongHieu.length > 0 && thuongHieu[0] !== "") {
        params.thuongHieuId = thuongHieu;
    }
    // Danh mục
    var danhMuc = $("#danhMucSelect").val();
    if (danhMuc && danhMuc.length > 0 && !(danhMuc.length === 1 && danhMuc[0] === "")) {
        params.danhMucIds = danhMuc;
    }
    // Chất liệu
    var chatLieu = $("#chatLieuSelect").val();
    if (chatLieu && chatLieu.length > 0 && chatLieu[0] !== "") {
        params.chatLieuId = chatLieu;
    }
    // Kiểu dáng
    var kieuDang = $("#kieuDangSelect").val();
    if (kieuDang && kieuDang.length > 0 && kieuDang[0] !== "") {
        params.kieuDangId = kieuDang;
    }
    // Màu sắc
    var mauSac = $("#mauSacSelect").val();
    if (mauSac && mauSac.length > 0 && mauSac[0] !== "") {
        params.mauSacId = mauSac;
    }
    // Lọc giá
    var selectedPriceRange = $("input[name='priceRange']:checked").val();
    if (selectedPriceRange) {
        if (selectedPriceRange === "0-200000") {
            params.minPrice = 0;
            params.maxPrice = 200000;
        } else if (selectedPriceRange === "200000-500000") {
            params.minPrice = 200000;
            params.maxPrice = 500000;
        } else if (selectedPriceRange === "500000-800000") {
            params.minPrice = 500000;
            params.maxPrice = 800000;
        } else if (selectedPriceRange === "800000-") {
            params.minPrice = 800000;
            // Không cần maxPrice cho "Trên 800.000 VNĐ"
        }
    }
    // Sắp xếp
    var sort = $("#sortSelect").val();
    if (sort) {
        if(sort === 'priceAsc') {
            params.orderBy = 'sanPhamChiTiets.giaBan';
            params.sortDirection = 'asc';
        } else if(sort === 'priceDesc') {
            params.orderBy = 'sanPhamChiTiets.giaBan';
            params.sortDirection = 'desc';
        } else if(sort === 'nameAsc') {
            params.orderBy = 'ten';
            params.sortDirection = 'asc';
        } else if(sort === 'nameDesc') {
            params.orderBy = 'ten';
            params.sortDirection = 'desc';
        }
    }
    // Nếu URL có parameter "ten"
    var urlParams = new URLSearchParams(window.location.search);
    if(urlParams.has('ten')) {
        params.ten = urlParams.get('ten');
        $("#product-title").text("Sản phẩm có tên: " + params.ten);
    } else {
        $("#product-title").text("Tất cả sản phẩm");
    }

    return params;
}
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
              <a href="/san-pham/${product.id}">
                <img src="${defaultImage}" class="card-img-top product-img" alt="${product.ten}" data-default="${defaultImage}" data-hover="${hoverImage}">
              </a>
              <div class="card-body d-flex flex-column">
                <h5 class="card-title">
                  <a href="/san-pham/${product.id}" class="text-decoration-none text-dark">${product.ten}</a>
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


function fetchProducts(queryParams) {
    queryParams.page = (typeof queryParams.page !== "undefined") ? queryParams.page : 0;
    queryParams.pageSize = 10;
    lastQuery = queryParams;

    $("#product-container").html('<div class="text-center my-3"><i class="fas fa-spinner fa-spin fa-2x"></i> Đang tải sản phẩm...</div>');

    $.ajax({
        url: '/api/san-pham',
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

function updateMainPrice(prod) {
    // Hàm định dạng tiền sang VND
    function formatCurrency1(value) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    }

    if (selectedColor && selectedSize) {
        var matchingDetail = prod.sanPhamChiTiets.find(function(ct) {
            return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
        });
        if (matchingDetail) {
            var giaSanPhamHtml = '';
            if (matchingDetail.isPromotionProduct) {
                // Trường hợp khuyến mại
                if (matchingDetail.dotGiamGia.loaiChietKhau === 'PHAN_TRAM') {
                    giaSanPhamHtml = `
                        <div class="p-0 d-flex align-items-end">
                            <h5 class="me-3 mr-2">Giá gốc:</h5>
                            <del class="h5 mt-2 price text-15 text-th">${formatCurrency1(matchingDetail.giaBan)}</del>
                            <span class="ml-1" style="color: red;"> - ${matchingDetail.dotGiamGia.giaTriGiam}%</span>
                        </div>
                        <div class="p-0 d-flex align-items-end">
                            <h5 class="me-3 p-0 mr-2" style="margin: 0;">Giá khuyến mại:</h5>
                            <div class="p-0">
                                <span class="h5 mt-2 price font-weight-bold">${formatCurrency1(matchingDetail.giaChietKhau)}</span>
                            </div>
                        </div>
                    `;
                } else {
                    // Trường hợp khuyến mại theo giá trị tuyệt đối
                    giaSanPhamHtml = `
                        <div class="p-0 d-flex align-items-end">
                            <h5 class="me-3">Giá gốc:</h5>
                            <del class="h5 mt-2 price text-15 text-th">${formatCurrency1(matchingDetail.giaBan)}</del>
                            <span class="ml-1"> - ${formatCurrency1(matchingDetail.dotGiamGia.giaTriGiam)}</span>
                        </div>
                        <div class="p-0 d-flex align-items-end">
                            <h5 class="mt-2" style="margin: 0;">Giá khuyến mại:</h5>
                            <div class="p-0">
                                <span class="h5 mt-2 price font-weight-bold">${formatCurrency1(matchingDetail.giaBan - matchingDetail.dotGiamGia.giaTriGiam)}</span>
                            </div>
                        </div>
                    `;
                }
            } else {
                // Không có khuyến mại
                giaSanPhamHtml = `
                    <div class="p-0 d-flex align-items-end">
                        <h5 class="me-3 p-0 m-0">Giá:</h5>
                        <div class="p-0">
                            <span class="h5 mt-2 price font-weight-bold">${formatCurrency1(matchingDetail.giaBan)}</span>
                        </div>
                    </div>
                `;
            }
            $("#modal-price").html(giaSanPhamHtml);
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
            // Xác định giá hiệu quả dựa trên khuyến mại
            var effectivePrice = matchingDetail.giaBan;
            if (matchingDetail.isPromotionProduct) {
                if (matchingDetail.dotGiamGia.loaiChietKhau === 'PHAN_TRAM') {
                    effectivePrice = matchingDetail.giaChietKhau;
                } else {
                    effectivePrice = matchingDetail.giaBan - matchingDetail.dotGiamGia.giaTriGiam;
                }
            }
            var totalAmount = quantity * effectivePrice;
            $("#modal-total").text("Tổng tiền: " + totalAmount.toLocaleString() + " VNĐ");
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
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Số lượng phải là số nguyên dương, ít nhất 1.'
        });
        return;
    }
    if (!selectedColor || !selectedSize) {
        Swal.fire({
            icon: 'error',
            title: 'Chưa chọn đủ!',
            text: 'Vui lòng chọn cả màu sắc và kích thước.'
        });
        return;
    }
    var matchingDetail = currentProduct.sanPhamChiTiets.find(function(ct) {
        return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
    });
    if (!matchingDetail) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Không tìm thấy chi tiết sản phẩm phù hợp!'
        });
        return;
    }
    if (matchingDetail.soLuong < quantity) {
        Swal.fire({
            icon: 'error',
            title: 'Hết hàng!',
            text: 'Số lượng tồn kho không đủ.'
        });
        return;
    }

    // Tính giá hiệu quả dựa trên khuyến mại nếu có
    var effectivePrice = matchingDetail.giaBan;
    if (matchingDetail.isPromotionProduct && matchingDetail.dotGiamGia && isDiscountActive(matchingDetail)) {
        if (matchingDetail.dotGiamGia.loaiChietKhau === 'PHAN_TRAM') {
            effectivePrice = matchingDetail.giaChietKhau;
        } else {
            effectivePrice = matchingDetail.giaBan - matchingDetail.dotGiamGia.giaTriGiam;
        }
    }

    var totalAmount = quantity * effectivePrice;
    if (totalAmount > 5000000) {
        Swal.fire({
            icon: 'error',
            title: 'Giới hạn mua!',
            text: 'Tổng tiền mua phải dưới 5 triệu VNĐ.'
        });
        return;
    }

    var info = {
        idSanPhamChiTiet: parseInt(matchingDetail.id),
        soLuong: parseInt(quantity),
    };
    console.log("Thông tin giỏ hàng:", info);
    fetch("/api/gio-hang", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(info),
    })
        .then(response => {
            console.log("Response status:", response.status);
            return response.json();
        })
        .then(data => {
            console.log("Phản hồi từ API:", data);
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


/***************** Các sự kiện khởi tạo *****************/
$(document).ready(function() {
    // Khi load trang, lấy tham số lọc từ URL (nếu có) và gọi API
    var initialParams = getFilterParams();
    fetchProducts(initialParams);

    // Sự kiện áp dụng filter khi submit form
    $("#filter-form").on("submit", function(e) {
        e.preventDefault();
        var queryParams = getFilterParams();
        queryParams.page = 0;
        fetchProducts(queryParams);
    });

    // Reset form filter và gọi lại API với các tham số rỗng (hoặc theo URL nếu có)
    $("#filter-form").on("reset", function() {
        setTimeout(function() {
            $("#thuongHieuSelect, #chatLieuSelect, #kieuDangSelect, #mauSacSelect").val("");
            $("#danhMucSelect").val("");
            $("#minPrice, #maxPrice, #sortSelect").val("");
            $("input[name='priceRange']").prop("checked", false);
            fetchProducts(getFilterParams());
        }, 100);
    });

    // Mở modal thêm giỏ hàng khi click
    $(document).on("click", ".add-to-cart-btn", function() {
        var productData = $(this).data("product");
        openAddToCartModal(productData);
    });
});