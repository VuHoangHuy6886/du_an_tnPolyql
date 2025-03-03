// // dependency
// Top
// <link href="/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet"/>
// <script src="/vendor/sweetalert2/sweetalert2.min.js"></script>
// Bottom
// <script src="/vendor/datatables/jquery.dataTables.min.js"></script>
//

const apiURL = "/api/v1/nhan-vien";
let _existingNames;
$(document).attr("title", "Quản lý tài khoản")
//Image upload evnt
$(document).ready(function(){

    $(".imageUpload").on("change input",function(event){
        let imageFile = event.target.files[0];
        let reader = new FileReader();
        reader.readAsDataURL(imageFile);
        reader.onload = (evt)=>{
            $(this).closest(".avatar-upload").find('img').attr('src', evt.target.result);
            $('.imagePreview').hide();
            $('.imagePreview').fadeIn(650);
        }
    });


});
function loadData() {
    let table = $('#dataTable').DataTable({

        "language": {
            "sProcessing": "Đang xử lý...",
            "sLengthMenu": "Hiển thị _MENU_ bản ghi",
            "sZeroRecords": "Không tìm thấy dữ liệu",
            "sInfo": "Hiển thị _START_ đến _END_ của _TOTAL_ bản ghi",
            "sInfoEmpty": "Hiển thị 0 đến 0 của 0 bản ghi",
            "sInfoFiltered": "(được lọc từ _MAX_ bản ghi)",
            "sInfoPostFix": "",
            "sSearch": "Tìm kiếm:",
            "sUrl": "",
            "oPaginate": {
                "sFirst": "Đầu",
                "sPrevious": "Trước",
                "sNext": "Tiếp",
                "sLast": "Cuối"
            }
        },
        "ajax": {
            "url": apiURL,
            "cache": true,
            "dataSrc": ""
        },
        "columnDefs": [
            {width: 30, targets: 0},
            {width: 300, targets: 1}
        ],
        "columns": [

            {
                data: "taiKhoan.anhUrl",
                render: function (data, type, row) {
                    return `<img src="${data}" alt="Ảnh đại diện" width="50" height="50">`;
                }
            },
            {data: "taiKhoan.email"},
            {data: "ten"},
            {data: "taiKhoan.soDienThoai"},
            {
                data: "taiKhoan.role",
                render: function (data, type, row) {
                    return `
                        <span class="badge badge-${data.toString().includes("ADMIN")?"danger":"primary"}">${data.toString().includes("ADMIN")?"Quản lý":"Nhân viên"}</span>
                    `
                }
            },
            {
                data: "taiKhoan",
                render: function (data, type, row) {
                    return `
                    ${data.googleId ? '<i class="fa-brands fa-google text-danger"></i>':""}
                    ${data.facebookId ? '<i class="fa-brands fa-facebook text-primary"></i>':""}
                    `
                }
            },
            {
                data: "taiKhoan.isEnable",
                render: function (data, type, row) {
                    return data?"Hoạt động":"Đã khóa"
                }
            }, {
                data: "taiKhoan",
                render: function (data, type, row) {
                    return `
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-edit">
                          Chỉnh sửa
                        </button>
                        <button type="button" class="btn btn-danger" >
                          Xóa
                        </button>
                    `
                }
            }


        ],
        "order": [[0, 'desc']]

    });
    let selectedStatus = $('#statusFilter').val();
    if (selectedStatus) {
        table.column(2).search('^' + selectedStatus + '$', true, false).draw();

    } else {
        // Xóa bộ lọc nếu không có gì được chọn
        table.column(2).search('').draw();

    }
    ;
}

const reloadDataTable = () => {
    $('#dataTable').DataTable().ajax.reload();
}

//config dataTable
$(document).ready(loadData());

//Reload event
$(document).ready(() => {
    $('#btn-reload').on("click", () => {
        reloadDataTable()
    })
})

//Add chat lieu
$(document).ready(() => {
    let existingNames;
    //Event
    $('#btn-add').on('click', () => {
        clearForm();
        existingNames = $('#dataTable').DataTable().column(1).data().toArray().map(name => name.toLowerCase());
        $('#modal-add').modal('show');
        console.log(existingNames)
    })
    //Validate exitst
    $('#add-ten').on('input paste', function () {
        let ten = $('#add-ten').val().toLowerCase().trim();
        if (existingNames.includes(ten) || ten.trim() === "") {
            $('#add-ten').addClass('is-invalid');
            $('#add-ten').removeClass('is-valid');
        } else {
            $('#add-ten').removeClass('is-invalid');
            $('#add-ten').addClass('is-valid');
        }
    });
    //Core
    $('#form-add').submit(function (event) {
        event.preventDefault(); // Ngăn chặn submit mặc định của form
        const form = $(this);
        if (!form[0].checkValidity()) {
            // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
            event.stopPropagation();
            form.addClass('was-validated');
        } else {
            let formData = {
                ten: $('#add-ten').val()
            }
            // Nếu form hợp lệ, gửi dữ liệu form lên server
            $.ajax({
                url: apiURL, // Thay 'URL_API' bằng đường dẫn của API của bạn
                method: 'PUT', // Phương thức HTTP
                data: JSON.stringify(formData),
                contentType: 'application/json',
                success: function (response) {
                    Toast.fire({
                        icon: "success",
                        title: "Thêm mới thành công"
                    })
                    $('#modal-add').modal('hide');
                    reloadDataTable();
                    console.log(response);
                },
                error: function (xhr, status, error) {
                    Toast.fire({
                        icon: "error",
                        title: "Thêm mới thất bại"
                    });
                    reloadDataTable();
                    console.log(response);
                    console.error(xhr.responseText);
                }
            });

        }
    });
})
//Edit
$(document).ready(() => {
    let existingNames;
    //Event
    $('#dataTable tbody').on('click', '.btn-edit', function () {
        clearForm()
        let rowData = $('#dataTable').DataTable().row($(this).closest('tr')).data();
        if (rowData) {
            // Lấy dữ liệu từ hàng
            let id = rowData.id;
            let ten = rowData.ten;
            let isDeleted = rowData.isDeleted;

            // Binding dữ liệu vào modal
            $('#edit-id').val(id);
            $('#edit-ten').val(ten);
            $('#edit-deleted').html(`
                <option value="false">Hoạt động</option>
                <option ${isDeleted ? "selected" : ""}  value="true">Đã xóa</option>`)
            if (isDeleted) {
                $('#edit-deleted').parent(".form-group").removeClass("d-none");
            } else {
                $('#edit-deleted').parent(".form-group").addClass("d-none");

            }
            // Hiển thị modal
            $('#modal-edit').modal('show');
            existingNames = $('#dataTable').DataTable().column(1).data().toArray().filter(elm => elm !== ten).map(name => name.toLowerCase().trim());
            console.log(existingNames)

        }

    });
    //Validate
    $('#edit-ten').off();
    $('#edit-ten').on('input paste', function () {
        let ten = $('#edit-ten').val().toLowerCase().trim();
        if (existingNames.includes(ten) || ten.trim() === "") {
            $('#edit-ten').addClass('is-invalid');
            $('#edit-ten').removeClass('is-valid');
        } else {
            $('#edit-ten').removeClass('is-invalid');
            $('#edit-ten').addClass('is-valid');
        }
    });
    //Core
    $('#form-edit').submit(function (event) {
        event.preventDefault(); // Ngăn chặn submit mặc định của form
        const form = $(this);
        if (!form[0].checkValidity()) {
            // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
            event.stopPropagation();
            form.addClass('was-validated');
        } else {
            let formData = {
                id: $('#edit-id').val(),
                ten: $('#edit-ten').val(),
                isDeleted: $('#edit-deleted').val()
            }
            // Nếu form hợp lệ, gửi dữ liệu form lên server
            $.ajax({
                url: apiURL, // Thay 'URL_API' bằng đường dẫn của API của bạn
                method: 'POST', // Phương thức HTTP
                data: JSON.stringify(formData),
                contentType: 'application/json',
                success: function (response) {
                    Toast.fire({
                        icon: "success",
                        title: "Cập nhật thành công"
                    })
                    $('#modal-edit').modal('hide');
                    reloadDataTable();
                    console.log(response);
                },
                error: function (xhr, status, error) {
                    Toast.fire({
                        icon: "error",
                        title: "Cập nhật thất bại"
                    });
                    reloadDataTable();
                    console.log(response);
                    console.error(xhr.responseText);
                }
            });

        }
    });
})

//Delete
$(document).ready(() => {
    $('#dataTable tbody').on('click', '.btn-delete', function () {
        let rowData = $('#dataTable').DataTable().row($(this).closest('tr')).data();
        if (rowData) {
            // Lấy dữ liệu từ hàng
            let id = rowData.id;
            let ten = rowData.ten;
            // Hiển thị modal

            Swal.fire({
                title: `Bạn chắc chắn muốn xóa ${ten}?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Hủy",
                confirmButtonText: "Xác nhận"
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: apiURL + '?id=' + id,
                        type: 'DELETE',
                        success: function () {
                            Toast.fire({
                                icon: "success",
                                title: "Xóa thành công"
                            })
                            reloadDataTable();
                        },
                        error: () => {
                            Toast.fire({
                                icon: "error",
                                title: "Xóa thất bại"
                            })
                            reloadDataTable();
                        }

                    })
                }
            });


        }

    });


})


//Revert
$(document).ready(() => {
    $('#dataTable tbody').on('click', '.btn-revert', function () {
        let rowData = $('#dataTable').DataTable().row($(this).closest('tr')).data();
        if (rowData) {
            // Lấy dữ liệu từ hàng
            let id = rowData.id;
            let ten = rowData.ten;
            // Hiển thị modal
            Swal.fire({
                title: `Bạn chắc chắn muốn khôi phục ${ten}?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Hủy",
                confirmButtonText: "Xác nhận"
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: apiURL + '/revert?id=' + id,
                        type: 'POST',
                        success: function () {
                            Toast.fire({
                                icon: "success",
                                title: "Khôi phục thành công"
                            })
                            reloadDataTable();
                        },
                        error: () => {
                            Toast.fire({
                                icon: "error",
                                title: "Khôi phục thất bại"
                            })
                            reloadDataTable();
                        }
                    })
                }
            });


        }

    });


})

//Filter
$(document).ready(function () {
    // Khởi tạo DataTable
    let table = $('#dataTable').DataTable();

    // Lắng nghe sự kiện thay đổi của select option
    $('#statusFilter').on('change', function () {
        let selectedStatus = $('#statusFilter').val();

        if (selectedStatus) {
            // Áp dụng bộ lọc theo cột Status
            table.column(2).search('^' + selectedStatus + '$', true, false).draw();

        } else {
            // Xóa bộ lọc nếu không có gì được chọn
            table.column(2).search('').draw();

        }
    });
});

//Import file
$(document).ready(function () {
    //Event
    $('#btn-import').on("click", (event) => {
        $('#import-file').click();
        $('#import-file').val("");
    })
    //Core
    $('#import-file').on("change", (event) => {
        dataToJson(event)
            .then(jsonData => validate_import(jsonData)
                .then(
                    jsonData => {
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

                    }
                )
            )
            .catch(e =>
                showErrorToast("Lỗi: " + e)
            )
    })
    //Validate
    const validate_import = (jsonData) => {
        return new Promise((resolve, reject) => {
            for (let obj of jsonData) {
                // Kiểm tra field

                if (!obj.hasOwnProperty('ten')) {
                    reject("Lỗi định dạng: Thiếu tên tài khoản")
                }
            }
            resolve(jsonData)
        })
    }
    //Import
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
                })
                Toast.fire({
                    icon: "success",
                    title: "Đã thay đổi " + data.length + " bản ghi"
                })
                reloadDataTable();
            },
            error: (jqXHR, textStatus, errorThrown) => {
                showErrorToast(`Lỗi: ${textStatus} - ${errorThrown}`);
                reloadDataTable();
            }


        })
    }

});

