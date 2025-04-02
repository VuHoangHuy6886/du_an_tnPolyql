const apiURL = "/api/san-pham";

const TrangThai = new Map([
    ['IN_STOCK', "Còn hàng"],
    ['OUT_OF_STOCK', "Hết hàng"],
    ['TEMPORARILY_OUT_OF_STOCK', "Hết hàng tạm thời"],
    ['COMING_SOON', "Sắp ra mắt"],
    ['DISCONTINUED', "Không kinh doanh"]
]);

$(document).attr("title", "Quản lý sản phẩm");

// Hàm load DataTable
function loadData() {
    return $('#dataTable').DataTable({
        destroy: true,
        processing: true,
        language: {
            sProcessing: "Đang tải dữ liệu...",
            sLengthMenu: "Hiển thị _MENU_ bản ghi",
            sZeroRecords: "Không tìm thấy dữ liệu",
            sInfo: "Hiển thị _START_ đến _END_ của _TOTAL_ bản ghi",
            sInfoEmpty: "Hiển thị 0 đến 0 của 0 bản ghi",
            sInfoFiltered: "(được lọc từ _MAX_ bản ghi)",
            sSearch: "Tìm kiếm:",
            oPaginate: {
                sFirst: "Đầu",
                sPrevious: "Trước",
                sNext: "Tiếp",
                sLast: "Cuối"
            }
        },
        ajax: {
            url: "/api/san-pham",
            type: "GET",
            cache: true,
            dataSrc: function (json) {
                console.log("Dữ liệu trả về:", json);
                // Lấy mảng dữ liệu từ thuộc tính "content"
                return json.content || [];
            }
        },
        columns: [
            {data: "id", name: "id"},
            {data: "maSanPham", name: "maSanPham"},
            {
                data: "ten",
                name: "ten",
                render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "thuongHieu",
                name: "thuongHieu",
                render: data => data && data.ten ? data.ten : "Không có dữ liệu"
            },
            {
                data: "chatLieu",
                name: "chatLieu",
                render: data => data && data.ten ? data.ten : "Không có dữ liệu"
            },
            {
                data: "kieuDang",
                name: "kieuDang",
                render: data => data && data.ten ? data.ten : "Không có dữ liệu"
            },
            {
                data: "danhMucs",
                name: "danhMucs",
                render: data => data && data.length ? data.map(dm => dm.ten).join(", ") : "Không có dữ liệu"
            },
            {
                data: "soLuong",
                name: "soLuong",
                render: data => data !== null && data !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "trangThai",
                name: "trangThai",
                visible: false,
                searchable: true,
                render: data => {
                    let text = data && data.trim() !== "" ? data : "Không có dữ liệu";
                    let ht = "";
                    let bgColor = "";
                    if (text === "CON_HANG") {
                        ht = "Còn hàng"
                        bgColor = "green";
                    } else if (text === "HET_HANG") {
                        ht = "Hết hàng"
                        bgColor = "red";
                    }
                    return `<span style="background-color:${bgColor}; color:white; padding:3px 6px; border-radius:4px; font-size: 13px;">${ht}</span>`;
                }
            },
            {
                data: "isDeleted",
                name: "tinhTrang",
                render: data => {
                    if (data === true) {
                        return `<span style="background-color:red; color:white; padding:2px 4px; border-radius:4px; font-size: 10px;">Ngưng bán</span>`;
                    } else {
                        return `<span style="background-color:green; color:white; padding:2px 4px; border-radius:4px; font-size: 10px;">Đang bán</span>`;
                    }
                }
            },
            {
                data: "anhUrl",
                name: "anhUrl",
                render: data => data ? `<img src="${data}" style="width:70px; height:100px;" alt="Ảnh">` : "Không có ảnh"
            },
            {
                data: "id",
                render: function (data, type, row) {
                    // Giả sử bạn đã có biến toggleBtn để hiển thị nút "Ngừng bán/Bán lại" như sau:
                    let toggleBtn = "";
                    if (row.isDeleted === false) {
                        // Sản phẩm đang bán
                        toggleBtn = `
              <button type="button" class="btn btn-sm btn-danger mr-2 btn-toggle-sell"
                      data-id="${data}" data-action="stop" title="Ngừng bán">
                  <i class="fa-solid fa-ban"></i>
              </button>
          `;
                    } else {
                        // Sản phẩm đã ngưng bán
                        toggleBtn = `
              <button type="button" class="btn btn-sm btn-success mr-2 btn-toggle-sell"
                      data-id="${data}" data-action="resume" title="Bán lại">
                  <i class="fa-solid fa-play"></i>
              </button>
          `;
                    }

                    // Tạo hàng chứa ba nút (chỉnh sửa, xem chi tiết, ngừng/bán lại)
                    // Và bên dưới là nút "Chỉnh sửa chi tiết" dẫn tới /admin/san-pham/<id>
                    return `
      <div class="d-flex flex-column align-items-end">
          <!-- Hàng 1: 3 nút icon -->
          <div class="d-flex justify-content-end align-items-center mb-2">
              <!-- Nút Chỉnh sửa -->
              <button type="button" class="btn btn-sm btn-primary mr-2 btn-edit" 
                      data-id="${data}" title="Chỉnh sửa nhanh">
                  <i class="fa-solid fa-pen-to-square" style="color: #ffffff;"></i>
              </button>
              <!-- Nút Xem chi tiết -->
              <button type="button" class="btn btn-sm btn-info mr-2 btn-detail"
                      data-id="${data}" title="Xem chi tiết">
                  <i class="fa-solid fa-eye" style="color: #ffffff;"></i>
              </button>
              <!-- Nút Ngừng bán/Bán lại -->
              ${toggleBtn}
          </div>

          <!-- Hàng 2: Nút dẫn tới /admin/san-pham/<id> -->
          <!-- Bạn có thể điều chỉnh style="width:..." để căn chiều rộng theo ý muốn -->
          <div style="width: 100%;">
              <a href="/admin/san-pham/${data}" class="btn btn-secondary w-100" title="Chỉnh sửa chi tiết đầy đủ">
                  <i class="fa-solid fa-pen-to-square"></i> Chỉnh sửa
              </a>
          </div>
      </div>
      `;
                }
            }

        ],
        order: [[0, 'desc']]
    });
}

const reloadDataTable = () => {
    $('#dataTable').DataTable().ajax.reload();
};

// Model detail
// Biến toàn cục lưu ID sản phẩm hiện tại (để reload bảng chi tiết)
let currentProductId = null;
let currentProductName = '';

function loadProductDetails(productId) {
    currentProductId = productId;
    $.ajax({
        url: `${apiURL}/${productId}`,
        type: 'GET',
        success: function (data) {
            currentProductName = data.ten || '';
            // Nếu bảng đã được khởi tạo với DataTable, hủy bỏ trước khi cập nhật dữ liệu mới
            if ($.fn.DataTable.isDataTable('#detailTable')) {
                $('#detailTable').DataTable().clear().destroy();
            }

            let tbody = $('#detailTable tbody');
            tbody.empty();
            // Lấy danh sách chi tiết từ thuộc tính sanPhamChiTiets của sản phẩm
            let details = data.sanPhamChiTiets || [];
            if (details.length > 0) {
                details.forEach(function (detail) {
                    let formattedPrice = detail.giaBan
                        ? new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(detail.giaBan)
                        : '';

                    // Lấy hình ảnh đầu tiên của màu tương ứng
                    let imageUrl = '';
                    if (data.anhs && data.anhs.length > 0 && detail.mauSac && detail.mauSac.id) {
                        const imagesOfColor = data.anhs.filter(img => img.mauSacId === detail.mauSac.id && !img.isDeleted);
                        if (imagesOfColor.length > 0) {
                            // Tìm ảnh có isDefault === true
                            const defaultImage = imagesOfColor.find(img => img.isDefault);
                            // Nếu tìm thấy thì gán url của ảnh đó, nếu không thì lấy ảnh đầu tiên
                            imageUrl = defaultImage ? defaultImage.url : imagesOfColor[0].url;
                        }
                    }

                    let row = `<tr data-detail-id="${detail.id}">
                        <td>${detail.id}</td>
                        <td>${data.ten || ''}</td>
                        <td>${detail.kichThuoc && detail.kichThuoc.ten ? detail.kichThuoc.ten : ''}</td>
                        <td>${formattedPrice}</td>
                        <td>${detail.soLuong || ''}</td>
                        <td>${detail.mauSac && detail.mauSac.ten ? detail.mauSac.ten : ''}</td>
                        <td>${imageUrl ? `<img src="${imageUrl}" style="width:70px; height:100px;" alt="Ảnh">` : ''}</td>
                        <td>
                            <button type="button" class="btn btn-sm btn-warning btn-edit-detail" data-id="${detail.id}">
                                <i class="fas fa-edit"></i> Sửa
                            </button>
                        </td>
                    </tr>`;
                    tbody.append(row);
                });
            } else {
                tbody.append('<tr><td colspan="8" class="text-center">Không có dữ liệu chi tiết</td></tr>');
            }

            // Khởi tạo DataTable sau khi đã load xong dữ liệu
            $('#detailTable').DataTable({
                // Các tùy chọn bạn muốn cấu hình (nếu cần), ví dụ:
                // paging: true,
                // searching: true,   // mặc định tìm kiếm đã bật
                // ordering: true,
                // info: true
            });
            $('#modal-product-detail').modal('show');
        },
        error: function (xhr, status, error) {
            console.error(xhr.responseText);
            Swal.fire('Lỗi', 'Không thể tải chi tiết sản phẩm', 'error');
        }
    });
}
$(document).on("click", ".btn-toggle-sell", function (e) {
    e.preventDefault();
    let id = $(this).data("id");
    let action = $(this).data("action");

    if (action === "stop") {
        // Xác nhận ngừng bán sản phẩm
        Swal.fire({
            title: 'Bạn có chắc muốn ngừng bán sản phẩm này?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Ngừng bán',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/api/san-pham/delete?id=" + id,
                    type: "DELETE",
                    success: function (response) {
                        Swal.fire("Thành công", "Sản phẩm đã ngừng bán", "success");
                        reloadDataTable();
                    },
                    error: function (xhr, status, error) {
                        Swal.fire("Lỗi", "Không thể ngừng bán sản phẩm", "error");
                    }
                });
            }
        });
    } else if (action === "resume") {
        // Xác nhận bán lại sản phẩm
        Swal.fire({
            title: 'Bạn có chắc muốn bán lại sản phẩm này?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Bán lại',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "/api/san-pham/revert?id=" + id,
                    type: "POST",
                    success: function (response) {
                        Swal.fire("Thành công", "Sản phẩm đã được bán lại", "success");
                        reloadDataTable();
                    },
                    error: function (xhr, status, error) {
                        Swal.fire("Lỗi", "Không thể bán lại sản phẩm", "error");
                    }
                });
            }
        });
    }
});

$(document).on("click", "#btnOpenModalAddDetail", function (e) {
    // Đảm bảo rằng currentProductName và currentProductId đã có giá trị từ loadProductDetails
    if (!currentProductName || !currentProductId) {
        Swal.fire("Lỗi", "Chưa có thông tin sản phẩm, vui lòng thử lại sau", "error");
        return;
    }
    // Điền tên sản phẩm (readonly)
    $("#productName").val(currentProductName);
    // Reset các trường khác trong modal
    $("#edit-detail-kich-thuoc").val('');
    $("#edit-detail-mau-sac").val('');
    $("#add-detail-quantity").val('');
    // Nếu có trường giá hoặc barcode, có thể reset hoặc điền mặc định
    $("#add-detail-price").val('');
    $("#barcode").val('');
    $("#addProductDetailModal").modal("show");
});

// Xử lý sự kiện thêm sản phẩm chi tiết
$("#btnAddProductDetail").on("click", function (e) {
    let form = document.getElementById("addProductDetailForm");

    // Kiểm tra tính hợp lệ của form
    if (!form.checkValidity()) {
        // Ngăn submit và dừng sự kiện
        e.preventDefault();
        e.stopPropagation();
        // Thêm lớp .was-validated để Bootstrap hiển thị lỗi
        form.classList.add("was-validated");
    } else {
        // Form hợp lệ => xử lý tiếp (gửi AJAX)
        // Lấy dữ liệu
        let kichThuocId = $("#edit-detail-kich-thuoc").val();
        let mauSacId = $("#edit-detail-mau-sac").val();
        let giaBan = parseFloat($("#add-detail-price").val());
        let soLuong = parseInt($("#add-detail-quantity").val());

        // Tạo payload
        let payload = {
            sanPhamId: currentProductId, // ID sản phẩm, giả sử đã lưu ở ngoài
            kichThuocId: parseInt(kichThuocId),
            mauSacId: parseInt(mauSacId),
            giaBan: giaBan,
            soLuong: soLuong
        };

        // Gửi AJAX
        $.ajax({
            url: "/api/v1/san-pham-detail",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(payload),
            success: function (response) {
                Swal.fire("Thành công", "Thêm sản phẩm chi tiết thành công", "success");
                $("#addProductDetailModal").modal("hide");
                // Reload chi tiết và bảng sản phẩm
                if (currentProductId) {
                    loadProductDetails(currentProductId);
                    reloadDataTable();
                }
            },
            error: function (xhr, status, error) {
                Swal.fire("Lỗi", xhr.responseText || "Thêm sản phẩm chi tiết thất bại", "error");
            }
        });
    }
});
$('#addProductDetailModal').on('hidden.bs.modal', function () {
    // Xóa trạng thái validation, nhưng không reset dữ liệu nhập
    $('#addProductDetailForm').removeClass('was-validated');

    // Loại bỏ hiển thị lỗi của Bootstrap (nếu có)
    $('#addProductDetailForm .form-control').removeClass('is-invalid is-valid');
});
// Xử lý khi nhấn nút "Chi tiết" trên DataTable (chứa ID sản phẩm)
$(document).ready(() => {
    $('#dataTable tbody').on('click', '.btn-detail', function () {
        let productId = $(this).data('id');
        loadProductDetails(productId);
    });
});

// Khi nhấn nút "Sửa" trong bảng chi tiết, mở modal chỉnh sửa chi tiết
$(document).on('click', '.btn-edit-detail', function () {
    let tr = $(this).closest('tr');
    let detailId = tr.data('detail-id');

    // Lấy dữ liệu từ các cột
    let tenSanPham = tr.find('td').eq(1).text().trim();
    let kichThuoc = tr.find('td').eq(2).text().trim();
    // Lấy giá bán (cột thứ 4, index 3)
    let giaBanText = tr.find('td').eq(3).text().trim();
    let giaBanStr = giaBanText.replace(/\./g, '').replace(/,/g, '.').replace(/[^0-9.]/g, '');
    let giaBan = parseFloat(giaBanStr) || 0;
    // Lấy số lượng (cột thứ 5, index 4)
    let soLuong = parseInt(tr.find('td').eq(4).text().trim()) || 0;
    // Lấy tên màu (cột thứ 6, index 5)
    let mauSanPham = tr.find('td').eq(5).text().trim();

    // Gán dữ liệu vào form chỉnh sửa chi tiết
    $('#detail-id').val(detailId);
    $('#detail-ten-san-pham').val(`${tenSanPham} - Màu ${mauSanPham} (Size: ${kichThuoc})`);
    $('#detail-gia-ban').val(giaBan);
    $('#detail-so-luong').val(soLuong);

    $('#modal-edit-detail').modal('show');
});

// Xử lý submit form chỉnh sửa chi tiết sản phẩm
$('#form-edit-detail').on('submit', function (e) {
    e.preventDefault();
    let detailId = $('#detail-id').val();
    let newGiaBan = parseFloat($('#detail-gia-ban').val());
    let newSoLuong = parseInt($('#detail-so-luong').val());

    // Validate: giá bán > 1000 và số lượng >= 0
    if (newGiaBan <= 1000) {
        Swal.fire("Lỗi", "Giá bán phải lớn hơn 1000", "error");
        return;
    }
    if (newSoLuong < 0) {
        Swal.fire("Lỗi", "Số lượng phải lớn hơn hoặc bằng 0", "error");
        return;
    }

    $.ajax({
        // Giả sử endpoint cập nhật chi tiết sản phẩm được đặt tại đây
        url: `/api/san-pham-detail/update`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: detailId,
            giaBan: newGiaBan,
            soLuong: newSoLuong
        }),
        success: function (response) {
            Swal.fire("Thành công", "Cập nhật chi tiết sản phẩm thành công", "success");
            $('#modal-edit-detail').modal('hide');
            if (currentProductId) {
                loadProductDetails(currentProductId);
                reloadDataTable();
            }
        },
        error: function (xhr, status, error) {
            Swal.fire("Lỗi", "Cập nhật chi tiết sản phẩm thất bại", "error");
            console.error(xhr.responseText);
        }
    });
});

$(document).ready(function () {
    // Khởi tạo DataTable và lưu vào biến table
    let table = loadData();

    let existingNames; // Dùng cho validate tên khi chỉnh sửa filter-form

    // Reload DataTable khi nhấn nút "Làm mới"
    $('#btn-reload').on("click", () => {
        reloadDataTable();
    });

    // Lọc chung cho bảng
    $('#search-column').on('keyup', function () {
        table.search(this.value).draw();
    });

    // Lọc theo các bộ lọc theo cột (Trạng thái, Thương hiệu, Danh mục)
    function applyFilters() {
        table.columns().every(function () {
            var columnIndex = this.index();
            var inputValue = $('.filter-input[data-column="' + columnIndex + '"]').val();
            this.search(inputValue ? inputValue : '');
        });
        table.draw();
    }

    $('.filter-input').on('change keyup', applyFilters);

    // Reset các bộ lọc
    $('#reset-filters').on('click', function () {
        $('.filter-input').val('');
        table.search('').columns().search('').draw();
    });
    // Xử lý sự kiện "Chỉnh sửa" khi nhấn nút btn-edit trong DataTable
    $(document).ready(() => {

        let existingNames;
        const table = $('#dataTable').DataTable();

        // Hàm reset form edit
        function clearForm() {
            $('#form-edit')[0].reset();
            $('#form-edit').removeClass('was-validated');
            $('#edit-ten').removeClass('is-invalid is-valid');
        }

        // Hàm hỗ trợ binding giá trị cho select theo text hiển thị
        const setSelectValue = (selectId, displayText) => {
            let text = displayText ? displayText.toString().trim() : "";
            let option = $(selectId + " option").filter(function () {
                return $(this).text().trim() === text;
            });
            if (option.length > 0) {
                $(selectId).val(option.val());
            } else {
                $(selectId).val($(selectId + " option:first").val());
            }
        };

        // Khi click nút "Chỉnh sửa" trong DataTable
        $('#dataTable tbody').on('click', '.btn-edit', function () {
            clearForm();
            let rowData = table.row($(this).closest('tr')).data();
            if (rowData) {
                // Gán các trường cơ bản
                $('#edit-id').val(rowData.id);
                $('#edit-ma-san-pham').val(rowData.maSanPham);
                // Sửa trường tên: sử dụng "ten" thay cho "tenSanPham"
                $('#edit-ten').val(rowData.ten);

                // Binding các trường select: Thương hiệu, Chất liệu, Kiểu dáng
                setSelectValue("#edit-thuong-hieu", rowData.thuongHieu && rowData.thuongHieu.ten ? rowData.thuongHieu.ten : "");
                setSelectValue("#edit-chat-lieu", rowData.chatLieu && rowData.chatLieu.ten ? rowData.chatLieu.ten : "");
                setSelectValue("#edit-kieu-dang", rowData.kieuDang && rowData.kieuDang.ten ? rowData.kieuDang.ten : "");

                $('#modal-edit').modal('show');

                // Lấy danh sách tên sản phẩm (ngoại trừ tên hiện tại) để validate
                existingNames = table.column(2).data().toArray()
                    .filter(name => name !== rowData.ten)
                    .map(name => name.toLowerCase().trim());
            }
        });

        // Validate tên sản phẩm
        $('#edit-ten').off('input paste').on('input paste', function () {
            let tenVal = $(this).val().toLowerCase().trim();
            if ((existingNames && existingNames.includes(tenVal)) || tenVal === "") {
                $(this).addClass('is-invalid').removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid').addClass('is-valid');
            }
        });

        // Submit form chỉnh sửa, gửi JSON
        $('#form-edit').submit(function (event) {
            event.preventDefault();
            const form = $(this);
            let maSanPhamVal = $('#edit-ma-san-pham').val().trim();
            let tenVal = $('#edit-ten').val().trim();

            // Validate cơ bản
            if (maSanPhamVal === "" || tenVal === "") {
                form.addClass('was-validated');
                return;
            }

            // Tạo payload JSON
            let payload = {
                id: $('#edit-id').val(),
                maSanPham: maSanPhamVal,
                ten: tenVal,
                idThuongHieu: $('#edit-thuong-hieu').val() ? parseInt($('#edit-thuong-hieu').val()) : null,
                idChatLieu: $('#edit-chat-lieu').val() ? parseInt($('#edit-chat-lieu').val()) : null,
                idKieuDang: $('#edit-kieu-dang').val() ? parseInt($('#edit-kieu-dang').val()) : null
            };
            console.log(payload);
            $.ajax({
                url: apiURL + "/update",
                method: 'PUT',
                contentType: "application/json",
                data: JSON.stringify(payload),
                success: function (response) {
                    Swal.fire({
                        icon: "success",
                        title: "Cập nhật sản phẩm thành công"
                    });
                    $('#modal-edit').modal('hide');
                    reloadDataTable(); // Hàm reload bảng DataTable
                },
                error: function (xhr, status, error) {
                    Swal.fire({
                        icon: "error",
                        title: "Cập nhật sản phẩm thất bại"
                    });
                    reloadDataTable();
                    console.error(xhr.responseText);
                }
            });
        });
    });

    $(document).on("click", ".btn-delete", function (e) {
        e.preventDefault();
        let id = $(this).data("id");
        Swal.fire({
            title: 'Bạn có chắc muốn xóa sản phẩm này?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: apiURL + "?id=" + id,
                    type: "DELETE",
                    success: function (response) {
                        Swal.fire("Thành công", "Đã xóa sản phẩm", "success");
                        reloadDataTable();
                    },
                    error: function (xhr, status, error) {
                        Swal.fire("Lỗi", "Không thể xóa sản phẩm", "error");
                    }
                });
            }
        });
    });

    $(document).on("click", ".btn-revert", function (e) {
        e.preventDefault();
        let id = $(this).data("id");
        Swal.fire({
            title: 'Bạn có chắc muốn khôi phục sản phẩm này?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Khôi phục',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: apiURL + "/revert?id=" + id,
                    type: "POST",
                    success: function (response) {
                        Swal.fire("Thành công", "Đã khôi phục sản phẩm", "success");
                        reloadDataTable();
                    },
                    error: function (xhr, status, error) {
                        Swal.fire("Lỗi", "Không thể khôi phục sản phẩm", "error");
                    }
                });
            }
        });
    });


});
