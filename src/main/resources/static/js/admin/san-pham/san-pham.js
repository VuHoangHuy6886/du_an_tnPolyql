const apiURL = "/api/v1/san-pham";

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
        language: {
            sProcessing: "Đang xử lý...",
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
            url: "/api/v1/san-pham-table",
            type: "GET",
            cache: true,
            dataSrc: function (json) {
                console.log("Dữ liệu trả về:", json);
                return json || [];
            }
        },
        columns: [
            { data: "id", name: "id" },
            { data: "maSanPham", name: "maSanPham" },
            {
                data: "tenSanPham",
                name: "tenSanPham",
                render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "thuongHieu",
                name: "thuongHieu",
                render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "chatLieu",
                name: "chatLieu",
                render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "kieuDang",
                name: "kieuDang",
                render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "danhMuc",
                name: "danhMuc",
                render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "soLuong",
                name: "soLuong",
                render: data => data !== null && data !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "trangThai",
                name: "trangThai",
                render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
            },
            {
                data: "anhUrl",
                name: "anhUrl",
                render: data => data ? `<img src="${data}" style="width:70px; height:100px;" alt="Ảnh">` : "Không có ảnh"
            },
            {
                data: "id",
                render: function (data, type, row) {
                    return `<div class="d-flex justify-content-end">
                        <button type="button" class="btn btn-sm btn-primary mr-3 btn-edit" data-id="${data}"><i class="fa-solid fa-pen-to-square" style="color: #ffffff;"></i></button>
                        <button type="button" class="btn btn-sm btn-info btn-detail" data-id="${data}"><i class="fa-solid fa-eye" style="color: #ffffff;"></i></button>
                    </div>`;
                }
            }
        ],
        order: [[0, 'desc']]
    });
}

const reloadDataTable = () => {
    $('#dataTable').DataTable().ajax.reload();
};
//model detail
// Biến toàn cục lưu ID sản phẩm hiện tại (để reload bảng chi tiết)
let currentProductId = null;

// Hàm load danh sách chi tiết sản phẩm cho sản phẩm có id = productId
function loadProductDetails(productId) {
    currentProductId = productId;
    $.ajax({
        url: `/api/v1/san-pham/${productId}/details`,
        type: 'GET',
        success: function (data) {
            let tbody = $('#detailTable tbody');
            tbody.empty();
            if (data && data.length > 0) {
                data.forEach(function(detail) {
                    let formattedPrice = detail.giaBan
                        ? new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(detail.giaBan)
                        : '';
                    let row = `<tr data-detail-id="${detail.id}">
                        <td>${detail.id}</td>
                        <td>${detail.tenSanPham || ''}</td>
                        <td>${detail.kichThuoc || ''}</td>
                        <td>${formattedPrice}</td>
                        <td>${detail.soLuong || ''}</td>
                        <td>${detail.tenMau || ''}</td>
                        <td>${detail.anhUrlDetail ? `<img src="${detail.anhUrlDetail}" style="width:70px; height:100px;" alt="Ảnh">` : ''}</td>
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
            $('#modal-product-detail').modal('show');
        },
        error: function (xhr, status, error) {
            console.error(xhr.responseText);
            Swal.fire('Lỗi', 'Không thể tải chi tiết sản phẩm', 'error');
        }
    });
}

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
    let mauSanPham = tr.find('td').eq(5).text().trim();
    let kichThuoc = tr.find('td').eq(2).text().trim();

    // Lấy giá bán (cột thứ 4, index 3): Loại bỏ dấu phân cách và ký tự không phải số
    let giaBanText = tr.find('td').eq(3).text().trim();
    let giaBanStr = giaBanText.replace(/\./g, '').replace(/,/g, '.').replace(/[^0-9.]/g, '');
    let giaBan = parseFloat(giaBanStr) || 0;

    // Lấy số lượng (cột thứ 5, index 4)
    let soLuong = parseInt(tr.find('td').eq(4).text().trim()) || 0;

    // Gán dữ liệu vào form chỉnh sửa chi tiết
    $('#detail-id').val(detailId);
    // Nối chuỗi cho trường "Tên sản phẩm": hiển thị tên, màu, kích thước
    $('#detail-ten-san-pham').val(tenSanPham + " Màu " + mauSanPham + " ( Size: " + kichThuoc + " )");
    $('#detail-gia-ban').val(giaBan);
    $('#detail-so-luong').val(soLuong);

    $('#modal-edit-detail').modal('show');
});

// Xử lý submit form chỉnh sửa chi tiết sản phẩm
$('#form-edit-detail').on('submit', function (e) {
    e.preventDefault();
    let detailId = $('#detail-id').val();
    let newGiaBan = $('#detail-gia-ban').val();
    let newSoLuong = $('#detail-so-luong').val();

    $.ajax({
        url: `/api/v1/san-pham-detail/update`, // Endpoint cập nhật chi tiết sản phẩm trên server
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

//
$(document).ready(function () {
    // Khởi tạo DataTable và lưu vào biến table
    let table = loadData();
    let existingNames; // Dùng cho validate tên khi chỉnh sửa

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

    // Hiển thị modal "Thêm mới"
    $('#btn-add').on("click", () => {
        $('#modal-add').modal('show');
    });

    // Xử lý submit form "Thêm mới"
    $('#form-add').on("submit", function (e) {
        e.preventDefault();
        const data = {
            maSanPham: $('#add-ma-san-pham').val(),
            ten: $('#add-ten').val(),
            trangThai: $('#add-trang-thai').val(),
            moTa: $('#add-mo-ta').val(),
            anhUrl: $('#add-anh-url').val()
        };
        $.ajax({
            url: apiURL,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                $('#modal-add').modal('hide');
                reloadDataTable();
                Swal.fire("Thành công", "Đã thêm sản phẩm mới", "success");
            },
            error: function (xhr, status, error) {
                Swal.fire("Lỗi", "Không thể thêm sản phẩm", "error");
            }
        });
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
                $('#edit-ten').val(rowData.tenSanPham);

                // Binding các trường select: Thương hiệu, Chất liệu, Kiểu dáng
                setSelectValue("#edit-thuong-hieu", rowData.thuongHieu || "");
                setSelectValue("#edit-chat-lieu", rowData.chatLieu || "");
                setSelectValue("#edit-kieu-dang", rowData.kieuDang || "");

                $('#modal-edit').modal('show');

                // Lấy danh sách tên sản phẩm (ngoại trừ tên hiện tại) để validate
                existingNames = table.column(2).data().toArray()
                    .filter(name => name !== rowData.tenSanPham)
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
            console.log(payload)
            $.ajax({
                url: apiURL + "/update",
                method: 'POST',
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











    $(document).on("click", ".btn-delete", function(e) {
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
                    success: function(response) {
                        Swal.fire("Thành công", "Đã xóa sản phẩm", "success");
                        reloadDataTable();
                    },
                    error: function(xhr, status, error) {
                        Swal.fire("Lỗi", "Không thể xóa sản phẩm", "error");
                    }
                });
            }
        });
    });

    $(document).on("click", ".btn-revert", function(e) {
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
                    success: function(response) {
                        Swal.fire("Thành công", "Đã khôi phục sản phẩm", "success");
                        reloadDataTable();
                    },
                    error: function(xhr, status, error) {
                        Swal.fire("Lỗi", "Không thể khôi phục sản phẩm", "error");
                    }
                });
            }
        });
    });

    // Xử lý import file Excel (đoạn code giữ nguyên)
    $(document).ready(function () {
        // Event: Nhấn nút Import
        $('#btn-import').on("click", (event) => {
            $('#import-file').click();
            $('#import-file').val("");
        });
        // Core: Khi chọn file
        $('#import-file').on("change", (event) => {
            dataToJson(event)
                .then(jsonData => validate_import(jsonData)
                    .then(jsonData => {
                        Swal.fire({
                            title: "Bạn chắc chắn chứ ?",
                            text: "Sau khi import bạn sẽ không thể quay lại!",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#3085d6",
                            cancelButtonColor: "#d33",
                            cancelButtonText: "Hủy",
                            confirmButtonText: "Xác nhận"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                import_excel(jsonData);
                            }
                        });
                    })
                )
                .catch(e =>
                    showErrorToast("Lỗi: " + e)
                );
        });
        // Validate
        const validate_import = (jsonData) => {
            return new Promise((resolve, reject) => {
                for (let obj of jsonData) {
                    if (!obj.hasOwnProperty('ten')) {
                        reject("Lỗi định dạng: Thiếu tên SP");
                    }
                }
                resolve(jsonData);
            });
        };
        // Import
        const import_excel = (jsonData) => {
            $.ajax({
                url: apiURL + "/import-excel",
                type: 'POST',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(jsonData),
                success: function (data) {
                    Toast.fire({
                        icon: "success",
                        title: "Nhập excel thành công"
                    });
                    Toast.fire({
                        icon: "success",
                        title: "Đã thay đổi " + data.length + " bản ghi"
                    });
                    reloadDataTable();
                },
                error: (jqXHR, textStatus, errorThrown) => {
                    showErrorToast(`Lỗi: ${textStatus} - ${errorThrown}`);
                    reloadDataTable();
                }
            });
        };
    });
});
