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
$(document).ready(() => {
    // Sự kiện khi click nút "Chi tiết" trong DataTable (đã được thêm vào cột hành động)
    $('#dataTable tbody').on('click', '.btn-detail', function () {
        let productId = $(this).data('id');
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
                        let row = `<tr>
                            <td>${detail.id}</td>
                            <td>${detail.kichThuoc || ''}</td>
                            <td>${formattedPrice}</td>
                            <td>${detail.soLuong || ''}</td>
                            <td>${detail.tenMau || ''}</td>
                            <td>${detail.anhUrlDetail ? `<img src="${detail.anhUrlDetail}" style="width:70px; height:100px;" alt="Ảnh">` : ''}</td>
                        </tr>`;
                        tbody.append(row);
                    });
                } else {
                    tbody.append('<tr><td colspan="6" class="text-center">Không có dữ liệu chi tiết</td></tr>');
                }
                $('#modal-product-detail').modal('show');
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseText);
                Swal.fire('Lỗi', 'Không thể tải chi tiết sản phẩm', 'error');
            }
        });
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
    const apiURL = "/api/v1/san-pham";

    $(document).ready(() => {
        let existingNames;
        const table = $('#dataTable').DataTable();

        // Hàm reset form edit
        function clearForm() {
            $('#form-edit')[0].reset();
            $('#form-edit').removeClass('was-validated');
            $('#edit-ten').removeClass('is-invalid is-valid');
            $('#edit-anh-preview').attr('src', '');
        }

        // Cập nhật preview ảnh khi chọn file mới
        $('#edit-anh-file').on('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    $('#edit-anh-preview').attr('src', e.target.result);
                };
                reader.readAsDataURL(file);
            } else {
                $('#edit-anh-preview').attr('src', '');
            }
        });

        // Hàm hỗ trợ binding giá trị cho select dựa trên text hiển thị;
        // Nếu không tìm thấy option khớp, chọn option đầu tiên.
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
                // Binding các trường cơ bản
                $('#edit-id').val(rowData.id);
                $('#edit-ma-san-pham').val(rowData.maSanPham);
                $('#edit-ten').val(rowData.tenSanPham);
                $('#edit-trang-thai').val(rowData.trangThai);
                $('#edit-anh-url').val(rowData.anhUrl);

                // Binding các trường select dựa trên tên hiển thị (nếu không có dữ liệu, chọn option đầu tiên)
                setSelectValue("#edit-thuong-hieu", rowData.thuongHieu || "");
                setSelectValue("#edit-chat-lieu", rowData.chatLieu || "");
                setSelectValue("#edit-kieu-dang", rowData.kieuDang || "");
                setSelectValue("#edit-danh-muc", rowData.danhMuc || "");

                // Nếu có ảnh, hiển thị preview
                if (rowData.anhUrl) {
                    $('#edit-anh-preview').attr('src', rowData.anhUrl);
                } else {
                    $('#edit-anh-preview').attr('src', '');
                }

                // Hiển thị modal chỉnh sửa
                $('#modal-edit').modal('show');

                // Lấy danh sách tên sản phẩm (ngoại trừ tên hiện tại) để dùng cho validate (nếu cần)
                existingNames = table.column(2).data().toArray()
                    .filter(name => name !== rowData.tenSanPham)
                    .map(name => name.toLowerCase().trim());
            }
        });

        // Validate tên sản phẩm khi nhập (không trùng và không rỗng)
        $('#edit-ten').off('input paste').on('input paste', function () {
            let tenVal = $(this).val().toLowerCase().trim();
            if ((existingNames && existingNames.includes(tenVal)) || tenVal === "") {
                $(this).addClass('is-invalid').removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid').addClass('is-valid');
            }
        });

        // Submit form chỉnh sửa với FormData (hỗ trợ file upload)
        $('#form-edit').submit(function (event) {
            event.preventDefault();
            const form = $(this);

            // Chỉ validate Mã sản phẩm và Tên sản phẩm
            let maSanPhamVal = $('#edit-ma-san-pham').val().trim();
            let tenVal = $('#edit-ten').val().trim();
            if (maSanPhamVal === "" || tenVal === "") {
                form.addClass('was-validated');
                return;
            }

            let formData = new FormData();
            formData.append('id', $('#edit-id').val());
            formData.append('maSanPham', maSanPhamVal);
            formData.append('ten', tenVal);
            formData.append('trangThai', $('#edit-trang-thai').val());
            formData.append('anhUrl', $('#edit-anh-url').val());

            // Ép sang số nếu có giá trị, hoặc gửi rỗng
            let thuongHieuVal = $('#edit-thuong-hieu').val();
            formData.append('thuongHieu', thuongHieuVal ? parseInt(thuongHieuVal) : null);
            let chatLieuVal = $('#edit-chat-lieu').val();
            formData.append('chatLieu', chatLieuVal ? parseInt(chatLieuVal) : null);
            let kieuDangVal = $('#edit-kieu-dang').val();
            formData.append('kieuDang', kieuDangVal ? parseInt(kieuDangVal) : null);
            let danhMucVal = $('#edit-danh-muc').val();
            formData.append('danhMuc', danhMucVal ? parseInt(danhMucVal) : null);

            // Nếu có file ảnh mới được chọn, thêm file vào FormData
            let fileInput = $('#edit-anh-file')[0].files;
            if (fileInput.length > 0) {
                formData.append('anh', fileInput[0]);
            }


            $.ajax({
                url: apiURL, // API của bạn phải hỗ trợ multipart/form-data
                method: 'POST', // hoặc PUT
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    Toast.fire({
                        icon: "success",
                        title: "Cập nhật thành công"
                    });
                    $('#modal-edit').modal('hide');
                    reloadDataTable();
                },
                error: function (xhr, status, error) {
                    Toast.fire({
                        icon: "error",
                        title: "Cập nhật thất bại"
                    });
                    reloadDataTable();

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
