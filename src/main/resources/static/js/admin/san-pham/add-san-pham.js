var urlAPI = "/api/v1/admin/data-list-add-san-pham";


var dataList = {
    sanPham: [],
    bluetooth: [],
    mauSac: [],
    congNgheManHinh: [],
    congNghePin: [],
    congSac: [],
    cpu: [],
    doPhanGiai: [],
    doPhanGiaiCameraSau: [],
    denFlash: [],
    doPhanGiaiCameraTruoc: [],
    doSangToiDa: [],
    dungLuongPin: [],
    gps: [],
    heDieuHanh: [],
    hoTroSacToiDa: [],
    jackTaiNghe: [],
    khuyenMai: [],
    kichThuocKhoiLuong: [],
    loaiPin: [],
    manHinhRong: [],
    mangDiDong: [],
    matKinhCamUng: [],
    mauSac: [],
    ram: [],
    series: [],
    sim: [],
    ten: [],
    thietKe: [],
    tinhNangCamera: [],
    tinhNangDacBiet: [],
    wifi: [],
}
var variantChangeFlagForStep2=true;
var variantChangeFlagForStep3=true;
//Init component
$(document).ready(() => {
    //Image product upload
    // {
    //     function readURL(input) {
    //         if (input.files && input.files[0]) {
    //             var reader = new FileReader();
    //             reader.onload = function (e) {
    //                 $('#imagePreview').css('background-image', 'url(' + e.target.result + ')');
    //                 $('#imagePreview').hide();
    //                 $('#imagePreview').fadeIn(650);
    //             }
    //             // reader.readAsDataURL(input.files[0]);
    //         }
    //     }
    //
    //     $("#imageUpload").change(function () {
    //         readURL(this);
    //     });
    // }

    $('#imageUpload').on('change', function () {
        var file = this.files[0];
        var formData = new FormData();
        formData.append('image', file);

        $.ajax({
            url: 'http://localhost:8080/image/upload-temp',  // Thay 'YOUR_API_ENDPOINT' bằng URL của API của bạn
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                $('#sp-img').val(response[0].name);
                $('#imagePreview').css('background-image', 'url(' + response[0].url + ')');
                $('#imagePreview').hide();
                $('#imagePreview').fadeIn(650);
                console.log('File uploaded successfully', response);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('File upload failed', textStatus, errorThrown);
            }
        });
    });

})

//validate product.name
$(document).ready(() => {
    //Validate exitst
    let initFlag = true;
    let exitstName;
    $("#sp-ten").on('input paste', function () {
        variantChangeFlagForStep2=true;// Thay đổi biến thể
        if (initFlag) {
            initFlag = false;
            exitstName = dataList.ten.map(name => name.toLowerCase().trim().replace(/\s/g, ''));
        }

        let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
        if (exitstName.includes(ten) || ten.trim() === "") {
            $(this).addClass('is-invalid');
            $(this).removeClass('is-valid');
        } else {
            $(this).removeClass('is-invalid');
            $(this).addClass('is-valid');
        }
    });
})


//Hiển thị lớp phủ khi chưa load hết thuộc tính
$("#overlay").show();
$("#sidebarToggle").trigger("click")
$(document).ajaxStart(function () {
}).ajaxStop(function () {

});


//fucn clone data
const cloneData = new Promise(function (resolve, reject) {
    $.ajax({
            url: urlAPI,
            success: function (response) {
                resolve(response);
            },
            error: function (xhr, status, error) {
                reject(status);
            }
        }
    )
})

//clone data load to select
$(document).ready(function () {
    cloneData.then(function (data) {
        dataList = data;
        console.log(data.congNgheManHinh)
        $('#selected-template').select2({
            data: dataList.sanPham,
            placeholder: "Chọn sản phẩm mẫu"
        })

        $("#sp-mau-sac").select2({
            data: dataList.mauSac,
            placeholder: "Chọn màu sắc",
            closeOnSelect: false
        });
        $("#sp-cong-nghe-man-hinh").select2({
            data: dataList.congNgheManHinh
        });
        $("#sp-series").select2({
            data: dataList.series,
            placeholder: "Chọn series"
        });
        $("#sp-rom").select2({
            placeholder: "Chọn bộ nhớ",
            closeOnSelect: false
        });
        dataList.ram.forEach(function (item) {
            $('#dataList-ram').append('<option value="' + item + '"></option>');
        });
        $("#sp-khuyen-mai").select2({
            data: dataList.khuyenMai,
            placeholder: "Khuyến mại",
            closeOnSelect: false
        });

        //Data list Màn hình
        {
            dataList.doPhanGiai.forEach(function (item) {
                $('#dataList-doPhanGiai').append('<option value="' + item + '"></option>');
            });
            dataList.manHinhRong.forEach(function (item) {
                $('#dataList-manHinhRong').append('<option value="' + item + '"></option>');
            });
            dataList.doSangToiDa.forEach(function (item) {
                $('#dataList-doSangToiDa').append('<option value="' + item + '"></option>');
            });
            $("#sp-matKinhCamUng").select2({
                data: dataList.matKinhCamUng
            });

        }
        // Data list camera sau
        {
            dataList.doPhanGiaiCameraSau.forEach(function (item) {
                $('#dataList-doPhanGiaiCameraSau').append('<option value="' + item + '"></option>');
            });
            dataList.denFlash.forEach(function (item) {
                $('#dataList-denFlashCameraSau').append('<option value="' + item + '"></option>');
            });
            $("#sp-tinhNangCameraSau").select2({
                data: dataList.tinhNangCamera,
                placeholder: "Chọn tính năng camera...",
                closeOnSelect: false
            });
        }
        // Data list camera trước
        {
            dataList.doPhanGiaiCameraTruoc.forEach(function (item) {
                $('#dataList-doPhanGiaiCameraTruoc').append('<option value="' + item + '"></option>');
            });
            dataList.denFlash.forEach(function (item) {
                $('#dataList-denFlashCameraTruoc').append('<option value="' + item + '"></option>');
            });
            $("#sp-tinhNangCameraTruoc").select2({
                data: dataList.tinhNangCamera,
                placeholder: "Chọn tính năng camera...",
                closeOnSelect: false
            });
        }
        // Data list hệ điều hành cpu
        {

            $("#sp-heDieuHanh").select2({
                data: dataList.heDieuHanh,
                placeholder: "Chọn hệ điều hành"
            });
            $("#sp-cpu").select2({
                data: dataList.cpu,
                placeholder: "Chọn cpu"
            });
        }

        // Data list kết nối
        {

            dataList.mangDiDong.forEach(function (item) {
                $('#dataList-mangDiDong').append('<option value="' + item + '"></option>');
            });
            dataList.sim.forEach(function (item) {
                $('#dataList-sim').append('<option value="' + item + '"></option>');
            });
            $("#sp-ketNoi-wifi").select2({
                data: dataList.wifi,
                placeholder: "Chọn wifi"
            });
            $("#sp-ketNoi-gps").select2({
                data: dataList.gps,
                placeholder: "Chọn gps"
            });
            $("#sp-ketNoi-bluetooth").select2({
                data: dataList.bluetooth,
                placeholder: "Chọn bluetooth"
            });
            dataList.congSac.forEach(function (item) {
                $('#dataList-sac').append('<option value="' + item + '"></option>');
            });
            dataList.jackTaiNghe.forEach(function (item) {
                $('#dataList-taiNghe').append('<option value="' + item + '"></option>');
            });

        }

        // Pin và sạc
        {
            $("#sp-pin-congNghePin").select2({
                data: dataList.congNghePin,
                placeholder: "Chọn công nghệ pin",
                closeOnSelect: false
            });
            dataList.dungLuongPin.forEach(function (item) {
                $('#dataList-pin-dungLuong').append('<option value="' + item + '"></option>');
            });
            dataList.hoTroSacToiDa.forEach(function (item) {
                $('#dataList-pin-sacToiDa').append('<option value="' + item + '"></option>');
            });
        }
        // Thông tin chung
        {

            dataList.thietKe.forEach(function (item) {
                $('#dataList-ttc-thietKe').append('<option value="' + item + '"></option>');
            });
            dataList.chatLieu.forEach(function (item) {
                $('#dataList-ttc-chatLieu').append('<option value="' + item + '"></option>');
            });
            dataList.kichThuocKhoiLuong.forEach(function (item) {
                $('#dataList-ttc-kichThuocKhoiLuong').append('<option value="' + item + '"></option>');
            });
            dataList.thoiGianBaoHanh.forEach(function (item) {
                $('#dataList-ttc-thoiGianBaoHanh').append('<option value="' + item + '"></option>');
            });
            $("#sp-ttc-tinhNangDacBiet").select2({
                data: dataList.tinhNangDacBiet,
                placeholder: "Chọn tính năng đặc biệt...",
                closeOnSelect: false
            });
        }

        // Ẩn overlay và hiện sugget
        $("#overlay").hide();
        window.setTimeout(() => {
            $("#modal-template").modal('show');
        }, 1000)

        $('#sp-rom,#sp-mau-sac,#sp-ten').on('change',function (){
            variantChangeFlagForStep2=true;
            variantChangeFlagForStep3=true;
        })



    })

})

//Template data
$(document).ready(() => {

})

//optimize select
$(document).ready(() => {
    $('select[multiple="multiple"]').on('change', function (e) {
        console.log("optimize select");
        const values = $(this).val();
        if (values.includes('')) {
            let newValues = values.filter(item => item != '');
            $(this).val(newValues).trigger('change');
        }
    });
})


//event tab nav
$(document).ready(function () {
    $(this).attr("title", "Thêm sản phẩm mới")


    var navListItems = $('div.setup-panel div a'),
        allWells = $('.setup-content'),
        allNextBtn = $('.nextBtn');
    allWells.hide();
    allWells.removeClass("d-none")
    navListItems.click(function (e) {
        e.preventDefault();
        var $target = $($(this).attr('href')),
            $item = $(this);
        if (!$item.hasClass('disabled')) {
            navListItems.removeClass('btn-primary').addClass('btn-secondary');
            $item.removeClass('btn-secondary')
            $item.addClass('btn-primary');
            allWells.hide();
            $target.show();
            $target.find('input:eq(0)').focus();
        }
    });

    allNextBtn.click(function () {
        alert('ok')
        var curStep = $(this).closest(".setup-content"),
            curStepBtn = curStep.attr("id"),
            nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().next().children("a"),
            curInputs = curStep.find("input, select"),
            isValid = true;
        console.log(curInputs)
        curInputs.removeClass("is-invalid")
        for (var i = 0; i < curInputs.length; i++) {
            if (!curInputs[i].validity.valid) {
                isValid = false;
                $(curInputs[i]).addClass("is-invalid");
            } else {
                $(curInputs[i]).addClass("is-valid");
            }
        }

        if (isValid) {
            nextStepWizard.removeAttr('disabled').trigger('click');
        }
    });
    $('div.setup-panel div a.btn-primary').trigger('click');
});
//
//
// Đoạn mã JavaScript này được sử dụng để quản lý một form đăng ký đa bước trong trang web. Hãy đi từng bước để giải thích cách JavaScript hoạt động trong đoạn mã này:
//
//     1. Khi tài liệu đã sẵn sàng ($(document).ready()):
// javascript
// Sao chép mã
// $(document).ready(function () {
//     // Các đoạn mã JavaScript được đặt trong này sẽ được thực thi khi toàn bộ tài liệu HTML đã tải xong.
// });
// Đoạn mã bên trong sẽ được thực thi khi toàn bộ tài liệu HTML đã được tải xong.
// 2. Khai báo biến quan trọng:
//     javascript
// Sao chép mã
// var navListItems = $('div.setup-panel div a'),
//     allWells = $('.setup-content'),
//     allNextBtn = $('.nextBtn');
// navListItems: Là danh sách các thành phần <a> nằm trong các <div class="setup-panel">, đại diện cho các nút điều hướng giữa các bước.
//     allWells: Là danh sách tất cả các phần tử có lớp setup-content, đại diện cho các phần nội dung của từng bước.
//     allNextBtn: Là danh sách tất cả các nút có lớp nextBtn, đại diện cho các nút "Next" trong từng bước.
// 3. Ẩn tất cả các phần nội dung bước:
//     javascript
// Sao chép mã
// allWells.hide();
// Ban đầu, ẩn tất cả các phần nội dung của các bước để chỉ hiển thị bước đầu tiên.
// 4. Xử lý khi người dùng nhấn vào các nút điều hướng (navListItems):
// javascript
// Sao chép mã
// navListItems.click(function (e) {
//     e.preventDefault();
//     var $target = $($(this).attr('href')),
//         $item = $(this);
//
//     if (!$item.hasClass('disabled')) {
//         navListItems.removeClass('btn-primary').addClass('btn-default');
//         $item.addClass('btn-primary');
//         allWells.hide();
//         $target.show();
//         $target.find('input:eq(0)').focus();
//     }
// });
// Khi người dùng nhấn vào một trong các navListItems:
//     Ngăn chặn hành vi mặc định của thẻ <a> (chẳng hạn chuyển hướng).
// Tìm phần nội dung tương ứng để hiển thị ($target).
//     Làm cho nút điều hướng hiện tại trở thành nổi bật (btn-primary), các nút khác trở thành mặc định (btn-default).
// Ẩn tất cả các phần nội dung khác và chỉ hiển thị phần nội dung của bước tương ứng.
//     Đặt trỏ chuột vào trường nhập liệu đầu tiên của bước mới hiển thị.
// 5. Xử lý khi người dùng nhấn vào nút "Next" (allNextBtn):
// javascript
// Sao chép mã
// allNextBtn.click(function(){
//     var curStep = $(this).closest(".setup-content"),
//         curStepBtn = curStep.attr("id"),
//         nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().next().children("a"),
//         curInputs = curStep.find("input[type='text'],input[type='url']"),
//         isValid = true;
//
//     $(".form-group").removeClass("has-error");
//     for(var i=0; i<curInputs.length; i++){
//         if (!curInputs[i].validity.valid){
//             isValid = false;
//             $(curInputs[i]).closest(".form-group").addClass("has-error");
//         }
//     }
//
//     if (isValid)
//         nextStepWizard.removeAttr('disabled').trigger('click');
// });

// Khi người dùng nhấn vào một trong các nút "Next":
// Xác định bước hiện tại (curStep) và lấy ID của nó (curStepBtn).
//     Tìm nút điều hướng tới bước tiếp theo (nextStepWizard).
//     Kiểm tra tính hợp lệ của các trường nhập liệu trong bước hiện tại.
//     Nếu tất cả các trường nhập liệu hợp lệ (isValid), hủy bỏ thuộc tính disabled của nút điều hướng tiếp theo và kích hoạt sự kiện click trên nút đó.
// 6. Kích hoạt sự kiện click vào nút điều hướng đầu tiên khi tài liệu được tải xong:
//     javascript
// Sao chép mã
// $('div.setup-panel div a.btn-primary').trigger('click');
// Khi tài liệu đã được tải xong, tự động kích hoạt sự kiện click vào nút điều hướng đầu tiên (nút có lớp btn-primary).
// Tóm lại:
//     Đoạn mã này sử dụng jQuery để tạo ra một form đăng ký đa bước trong trang web. Nó quản lý hiển thị các bước và kiểm soát chuyển đổi giữa các bước dựa trên sự kiện click của người dùng và tính hợp lệ của các trường nhập liệu. Mỗi khi người dùng nhấn vào nút "Next", mã sẽ kiểm tra xem các trường nhập liệu có hợp lệ không và di chuyển sang bước tiếp theo nếu thỏa mãn điều kiện.

//Event add modal
{

//Add cpu
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-cpu').on('click', () => {
            clearForm();
            existingNames = dataList.cpu.map(cpu => cpu.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-cpu').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-cpu').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-cpu-ten').val(),
                    gpu: $('#add-cpu-gpu').val(),
                    link: $('#add-cpu-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/cpu",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-cpu').modal('hide');
                        dataList.cpu.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-cpu').append(option).trigger('change');
                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })

//Add he dieu hanh
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-he-dieu-hanh').on('click', () => {
            clearForm();
            existingNames = dataList.heDieuHanh.map(tnc => tnc.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-he-dieu-hanh').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-he-dieu-hanh-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-he-dieu-hanh').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-he-dieu-hanh-ten').val(),
                    link: $('#add-he-dieu-hanh-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/he-dieu-hanh",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-he-dieu-hanh').modal('hide');
                        dataList.heDieuHanh.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-heDieuHanh').append(option).trigger('change');

                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })

//Add tinh nang camera truoc
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-tinh-nang-camera-truoc').on('click', () => {
            clearForm();
            existingNames = dataList.tinhNangCamera.map(tnc => tnc.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-tinh-nang-camera').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-tinh-nang-camera-ten').off();
        $('#add-tinh-nang-camera-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-tinh-nang-camera').off()
        $('#form-add-tinh-nang-camera').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-tinh-nang-camera-ten').val(),
                    link: $('#add-tinh-nang-camera-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/tinh-nang-camera",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-tinh-nang-camera').modal('hide');
                        dataList.tinhNangCamera.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-tinhNangCameraTruoc').append(option).trigger('change');
                        let option2 = new Option(response.ten, response.id, false, false);
                        $('#sp-tinhNangCameraSau').append(option2).trigger('change');
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

//Add tinh nang camera sau
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-tinh-nang-camera-sau').on('click', () => {
            clearForm();
            existingNames = dataList.tinhNangCamera.map(tnc => tnc.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-tinh-nang-camera').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-tinh-nang-camera-ten').off()
        $('#add-tinh-nang-camera-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-tinh-nang-camera').off()
        $('#form-add-tinh-nang-camera').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-tinh-nang-camera-ten').val(),
                    link: $('#add-tinh-nang-camera-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/tinh-nang-camera",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-tinh-nang-camera').modal('hide');
                        dataList.tinhNangCamera.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-tinhNangCameraSau').append(option).trigger('change');
                        let option2 = new Option(response.ten, response.id, false, false);
                        $('#sp-tinhNangCameraTruoc').append(option2).trigger('change');

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

//Add mat kinh cam ung
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-mat-kinh-cam-ung').on('click', () => {
            clearForm();
            existingNames = dataList.matKinhCamUng.map(mkcu => mkcu.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-mat-kinh-cam-ung').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-mat-kinh-cam-ung-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-mat-kinh-cam-ung').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-mat-kinh-cam-ung-ten').val(),
                    link: $('#add-mat-kinh-cam-ung-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-mat-kinh-cam-ung').modal('hide');
                        dataList.matKinhCamUng.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-matKinhCamUng').append(option).trigger('change');
                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })


//Add series
    $(document).ready(() => {
        let existingNames;
        let initFlag = true;
        //Event
        $('#btn-add-series').on('click', () => {
            clearForm();
            if (initFlag) {
                initFlag = false;
                existingNames = dataList.series.map(series => series.text.toLowerCase().trim().replace(/\s/g, ''));
            }

            $('#modal-series').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-series-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-series-add').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-series-ten').val(),
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/series",
                    method: 'PUT',
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-series').modal('hide');
                        dataList.series.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-series').append(option).trigger('change');
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

//Add cong nghe man hinh
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-cong-nghe-man-hinh').on('click', () => {
            clearForm();
            existingNames = dataList.congNgheManHinh.map(chatLieu => chatLieu.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-cong-nghe-man-hinh').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-cong-nghe-man-hinh-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-cong-nghe-man-hinh').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-cong-nghe-man-hinh-ten').val(),
                    link: $('#add-cong-nghe-man-hinh-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh",
                    method: 'PUT',
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-cong-nghe-man-hinh').modal('hide');
                        dataList.congNgheManHinh.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-cong-nghe-man-hinh').append(option).trigger('change');

                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })

//Add mau sac
    $(document).ready(() => {
        //Evnt modal input color
        $('#add-mau-sac-code').on('input', function () {
            $('#add-mau-sac-ma').val($(this).val().toUpperCase())
        });
        $('#add-mau-sac-ma').on('input', function () {
            $('#add-mau-sac-code').val($(this).val())
        });


        let existingNames;
        //Event
        $('#btn-add-mau-sac').on('click', () => {
            clearForm();

            existingNames = dataList.mauSac.map(mauSac => mauSac.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#add-mau-sac-ma').val("#000000")
            $('#modal-add-mau-sac').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-mau-sac-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-mau-sac').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-mau-sac-ten').val(),
                    ma: $('#add-mau-sac-code').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/mau-sac", // Thay 'URL_API' bằng đường dẫn của API của bạn
                    method: 'PUT',
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-mau-sac').modal('hide');
                        dataList.mauSac.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-mau-sac').append(option).trigger('change');

                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })

//Add wifi
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-wifi').on('click', () => {
            clearForm();
            existingNames = dataList.wifi.map(w => w.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-wifi').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-wifi-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-wifi').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-wifi-ten').val(),
                    link: $('#add-wifi-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/wifi",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-wifi').modal('hide');
                        dataList.wifi.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-ketNoi-wifi').append(option).trigger('change');
                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })


//Add gps
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-gps').on('click', () => {
            clearForm();
            existingNames = dataList.gps.map(mkcu => mkcu.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-gps').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-gps-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-gps').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-gps-ten').val(),
                    link: $('#add-gps-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/gps",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-gps').modal('hide');
                        dataList.gps.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-ketNoi-gps').append(option).trigger('change');
                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })


//Add bluetooth
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-bluetooth').on('click', () => {
            clearForm();
            existingNames = dataList.bluetooth.map(b => b.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-bluetooth').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-bluetooth-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-bluetooth').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-bluetooth-ten').val(),
                    link: $('#add-bluetooth-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-bluetooth').modal('hide');
                        dataList.bluetooth.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-ketNoi-bluetooth').append(option).trigger('change');
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

//Add cong nghe pin
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-cong-nghe-pin').on('click', () => {
            clearForm();
            existingNames = dataList.congNghePin.map(mkcu => mkcu.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-cong-nghe-pin').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-cong-nghe-pin-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-cong-nghe-pin').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-cong-nghe-pin-ten').val(),
                    link: $('#add-cong-nghe-pin-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/cong-nghe-pin",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-cong-nghe-pin').modal('hide');
                        dataList.congNghePin.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-pin-congNghePin').append(option).trigger('change');
                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })


//Add tinh nang dac biet
    $(document).ready(() => {
        let existingNames;
        //Event
        $('#btn-add-tinh-nang-dac-biet').on('click', () => {
            clearForm();
            existingNames = dataList.tinhNangDacBiet.map(mkcu => mkcu.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-tinh-nang-dac-biet').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-tinh-nang-dac-biet-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-tinh-nang-dac-biet').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-tinh-nang-dac-biet-ten').val(),
                    link: $('#add-tinh-nang-dac-biet-link').val()
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/tinh-nang-dac-biet",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-tinh-nang-dac-biet').modal('hide');
                        dataList.tinhNangDacBiet.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-ttc-tinhNangDacBiet').append(option).trigger('change');
                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })

//Add khuyen mai
    $(document).ready(() => {
        //Init dateranger
        $('#add-khuyen-mai-date').daterangepicker({
            "showDropdowns": true,
            "showWeekNumbers": true,
            "showISOWeekNumbers": true,
            "timePicker": true,
            "autoApply": true,
            "timePicker24Hour": true,
            "locale": {
                "format": "DD/MM/YYYY hh:mm",
                "separator": " tới ",
                "applyLabel": "Chọn",
                "cancelLabel": "Hủy",
                "fromLabel": "Từ",
                "toLabel": "Tới",
                "customRangeLabel": "Tùy chọn",
                "weekLabel": "Tuần",

                "daysOfWeek": [
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "CN"
                ],
                "monthNames": [
                    "Tháng 1",
                    "Tháng 2",
                    "Tháng 3",
                    "Tháng 4",
                    "Tháng 5",
                    "Tháng 6",
                    "Tháng 7",
                    "Tháng 8",
                    "Tháng 9",
                    "Tháng 10",
                    "Tháng 11",
                    "Tháng 12"
                ],
                "firstDay": 0
            },
            "startDate": new Date().toLocaleDateString("vi-VN"),
            "minDate": new Date().toLocaleDateString("vi-VN")
        }, function(start, end, label) {
            //    TODO BEFORE SELECT
        });

        let existingNames;
        //Event
        $('#btn-add-khuyen-mai').on('click', () => {
            clearForm();
            existingNames = dataList.khuyenMai.map(b => b.text.toLowerCase().trim().replace(/\s/g, ''));
            $('#modal-add-khuyen-mai').modal('show');
            console.log(existingNames)
        })
        //Validate exitst
        $('#add-khuyen-mai-ten').on('input paste', function () {
            let ten = $(this).val().toLowerCase().trim().replace(/\s/g, '');
            if (existingNames.includes(ten) || ten.trim() === "") {
                $(this).addClass('is-invalid');
                $(this).removeClass('is-valid');
            } else {
                $(this).removeClass('is-invalid');
                $(this).addClass('is-valid');
            }
        });
        //Core
        $('#form-add-khuyen-mai').submit(function (event) {
            event.preventDefault(); // Ngăn chặn submit mặc định của form
            const form = $(this);
            if (!form[0].checkValidity()) {
                // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
                event.stopPropagation();
                form.addClass('was-validated');
            } else {
                let formData = {
                    ten: $('#add-khuyen-mai-ten').val(),
                    link: $('#add-khuyen-mai-link').val(),
                    thoiGianBatDau: $('#add-khuyen-mai-date').data('daterangepicker').startDate.format("DD-MM-YYYY hh:mm:ss"),
                    thoiGianKetThuc: $('#add-khuyen-mai-date').data('daterangepicker').endDate.format("DD-MM-YYYY hh:mm:ss")
                }
                // Nếu form hợp lệ, gửi dữ liệu form lên server
                $.ajax({
                    url: "/api/v1/san-pham-chi-tiet/khuyen-mai",
                    method: 'PUT', // Phương thức HTTP
                    data: JSON.stringify(formData),
                    contentType: 'application/json',
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $('#modal-add-khuyen-mai').modal('hide');
                        dataList.khuyenMai.unshift({id: response.id, text: response.ten})
                        let option = new Option(response.ten, response.id, false, true);
                        $('#sp-khuyen-mai').append(option).trigger('change');
                        console.log(response);
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: "Thêm mới thất bại"
                        });
                        console.log(response);
                        console.error(xhr.responseText);
                    }
                });

            }
        });
    })
}

//Core clone template
$(document).ready(() => {


    //Core
    $('#form-template').submit(function (event) {
        $('#modal-template').modal('hide');
        $("#overlay").show();
        event.preventDefault(); // Ngăn chặn submit mặc định của form
        const form = $(this);
        if (!form[0].checkValidity()) {
            // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
            event.stopPropagation();
            form.addClass('was-validated');
        } else {
            let id = $('#selected-template').val()
            // Nếu form hợp lệ, gửi dữ liệu form lên server
            $.ajax({
                url: "/api/v1/admin/data-list-add-san-pham/" + id,
                contentType: 'application/json',
                success: function (response) {
                    $("#overlay").hide();
                    Toast.fire({
                        icon: "success",
                        title: "Sao chép thành công"
                    })
                    fillData(response);
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

//Đẩy data lên form
const fillData = (spTemp) => {
    let dataTemp = {
        id: "",
        tenSanPham: "",
        sanPhamChiTietMauSacIds: [],
        sanPhamChiTietRoms: [],
        manHinhCongNgheManHinhId: "",
        manHinhDoPhanGiai: "",
        manHinhManHinhRong: "",
        manHinhDoSangToiDa: "",
        manHinhMatKinhCamUngId: "",
        cameraTruocDoPhanGiai: "",
        cameraTruocTinhNangCameraIds: [],
        cameraSauDoPhanGiai: "",
        cameraSauDenFlash: "",
        cameraSauTinhNangCameraIds: [],
        heDieuHanhVaCpuHeDieuHanhId: "",
        heDieuHanhVaCpuCpuId: "",
        ketNoiMangDiDong: "",
        ketNoiSim: "",
        ketNoiWifiIds: [],
        ketNoiGpsIds: [],
        ketNoiBluetoothIds: [],
        ketNoiCongSac: "",
        ketNoiJackTaiNghe: "",
        pinVaSacDungLuongPin: "",
        pinVaSacLoaiPin: "",
        pinVaSacHoTroSacToiDa: "",
        pinVaSacCongNghePinIds: [],
        thongTinChungThietKe: "",
        thongTinChungMauSac: "",
        thongTinChungKichThuocKhoiLuong: "",
        thongTinChungTinhNangDacBietIds: [],
        khuyenMaiIds: [],
        seriesId: "",
        ram: "",
        thoiGianBaoHanh: "",
        trangThai: "",
        stt: ""
    };
    dataTemp = spTemp;

    console.log(dataTemp)
    $('#sp-series').val(dataTemp.seriesId).trigger('change');
    $('#sp-rom').val(dataTemp.sanPhamChiTietRoms).trigger('change');
    $('#sp-ram').val(dataTemp.ram).trigger('change');
    $('#sp-mau-sac').val(dataTemp.sanPhamChiTietMauSacIds).trigger('change');
    $('#sp-cong-nghe-man-hinh').val(dataTemp.manHinhCongNgheManHinhId).trigger('change');
    $('#sp-doPhanGiai').val(dataTemp.manHinhDoPhanGiai).trigger('change');
    $('#sp-manHinhRong').val(dataTemp.manHinhManHinhRong).trigger('change');
    $('#sp-doSangToiDa').val(dataTemp.manHinhDoSangToiDa).trigger('change');
    $('#sp-matKinhCamUng').val(dataTemp.manHinhMatKinhCamUngId).trigger('change');
    $('#sp-cameraSau-doPhanGiai').val(dataTemp.cameraSauDoPhanGiai).trigger('change');
    $('#sp-tinhNangCameraSau').val(dataTemp.cameraSauTinhNangCameraIds).trigger('change');
    $('#sp-cameraSau-denFlash').val(dataTemp.cameraSauDenFlash).trigger('change');
    $('#sp-cameraTruoc-doPhanGiai').val(dataTemp.cameraTruocDoPhanGiai).trigger('change');
    $('#sp-tinhNangCameraTruoc').val(dataTemp.cameraTruocTinhNangCameraIds).trigger('change');
    $('#sp-heDieuHanh').val(dataTemp.heDieuHanhVaCpuHeDieuHanhId).trigger('change');
    $('#sp-cpu').val(dataTemp.heDieuHanhVaCpuCpuId).trigger('change');
    $('#sp-ketNoi-mangDiDong').val(dataTemp.ketNoiMangDiDong).trigger('change');
    $('#sp-ketNoi-sim').val(dataTemp.ketNoiSim).trigger('change');
    $('#sp-ketNoi-wifi').val(dataTemp.ketNoiWifiIds).trigger('change');
    $('#sp-ketNoi-gps').val(dataTemp.ketNoiGpsIds).trigger('change');
    $('#sp-ketNoi-bluetooth').val(dataTemp.ketNoiBluetoothIds).trigger('change');
    $('#sp-ketNoi-sac').val(dataTemp.ketNoiCongSac).trigger('change');
    $('#sp-ketNoi-taiNghe').val(dataTemp.ketNoiJackTaiNghe).trigger('change');
    $('#sp-pin-dungLuong').val(dataTemp.pinVaSacDungLuongPin).trigger('change');
    $('#sp-pin-loaiPin').val(dataTemp.pinVaSacLoaiPin).trigger('change');
    $('#sp-pin-sacToiDa').val(dataTemp.pinVaSacHoTroSacToiDa).trigger('change');
    $('#sp-pin-congNghePin').val(dataTemp.pinVaSacCongNghePinIds).trigger('change');
    $('#sp-ttc-tinhNangDacBiet').val(dataTemp.thongTinChungTinhNangDacBietIds).trigger('change');
    $('#sp-ttc-chatLieu').val(dataTemp.thongTinChungMauSac).trigger('change');
    $('#sp-ttc-kichThuocKhoiLuong').val(dataTemp.thongTinChungKichThuocKhoiLuong).trigger('change');
    $('#sp-ttc-thoiGianBaoHanh').val(dataTemp.thoiGianBaoHanh).trigger('change');
    $('#sp-ttc-thietKe').val(dataTemp.thongTinChungThietKe).trigger('change');

}


$(document).ready(() => {
    $("a[href='#step-2']").on("click", function () {
        if(variantChangeFlagForStep2){
            let fieldsetContainer = '';
            let dataRow = '';
            let imageRow = '';
            let statusRow = '';
            let count = 1;

            //Tạo dòng  giá nhập giá bán input cho từng màu sắc và hình ảnh của chúng
            $('#sp-mau-sac').select2('data').forEach(function (mauSac) {
                imageRow += `
                  <tr>
                        <td style="text-align: center;vertical-align: middle">${mauSac.text}<span class="d-none">${mauSac.id}</span></td>
                        <td style="text-align: center;vertical-align: middle">
                            <div>
                                <input type="file" id="img-${mauSac.id}" ms-id="${mauSac.id}" class="form-control-file d-none" accept=".png, .jpg, .jpeg" multiple>
                                <label for="img-${mauSac.id}" class="btn btn-primary">Chọn ảnh</label>
                            </div>
                        </td>
                        <td class="d-flex">
                            <div class="form-group col-6">
                                    <input type="text" class="form-control d-none" required>
                                    <div class="invalid-feedback">
                                        Vui lòng chọn ảnh tương ứng với màu sắc
                                    </div>
                             </div>
                        </td>
                  
                  </tr>
            `

                dataRow += `
             <tr class="san-pham-chi-tiet">
                <td><input type="checkbox"></td>
                <td>${count++}</td>
                <td>${mauSac.text}<span class="d-none ms-id">${mauSac.id}</span></td>
                <td><input ms-id="${mauSac.id}" class="form-control form-control-sm giaBan-input" type="text" placeholder="Giá bán" aria-label=".form-control-sm example" required></td>
                <td><select class="form-control w-100 sm" id="sp-trang-thai" required>
                                        <option value="IN_STOCK">Có sẵn</option>
                                        <option value="OUT_OF_STOCK">Hết hàng</option>
                                        <option value="TEMPORARILY_OUT_OF_STOCK">Hết hàng tạm thời</option>
                                        <option value="COMING_SOON">Sắp ra mắt</option>
                                        <option value="DISCONTINUED">Không kinh doanh</option>
                                    </select></td>
                
                <td><button type="button" class="btn btn-danger btn-sm  delete-spct">Xóa</button></td>
            </tr>
            `

            })

            //Tạo card input cho từng phiên bản rom
            $('#sp-rom').val().forEach(elem => {
                fieldsetContainer += `
                    <div class="card shadow m-2 w-100 ">
                            <div class="card-header py-3">
                                <div class="row">
                                    <h1 class="h3 text-gray-800 col-4">Phiên bản ${elem}</h1>
                                    <!-- Additional controls -->
                                    <div class="col-8 d-flex justify-content-sm-end align-items-center">
                                       
                                        <button href="#" class="btn btn-primary btn-icon-split mr-2">
                                        <span class="icon text-white-50">
                                            <i class="fas fa-rotate-right"></i>
                                        </span>
                                            <span class="text">Làm lại</span>
                                        </button>
                                        <button href="#" class="btn btn-primary btn-icon-split mr-2 btn-delete">
                                        <span class="icon text-white-50">
                                            <i class="fas fa-trash"></i>
                                        </span>
                                            <span class="text">Xóa phiên bản</span>
                                        </button>
                                        

                                    </div>
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-data" rom-id="${elem}">
                                        <thead>
                                        <tr>
                                            <th><input type="checkbox" class="checkAll"></th>
                                            <th>Số thứ tự</th>
                                            <th>Màu sắc</th>
                                            <th>Giá bán</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>

                                        </tr>
                                        </thead>
                                        <tbody>
                                            ${dataRow}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
            `

            })


            let tenSanPham=$('#sp-ten').val();
            let statusContainer=`
                <div class="card shadow m-2 w-100 ">
                    <div class="card-header py-3">
                        <div class="row">
                            <h1 class="h3 text-gray-800 col-4">Trạng thái sản phẩm</h1>
                            <!-- Additional controls -->
                            
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-data" id="sp-spct-status"">
                                <thead>
                                <tr>
                                    <th>Tên phiên bản</th>
                                    <th>Màu sắc</th>
                                    <th>Giá bán</th>
                                    <th>Thao tác</th>

                                </tr>
                                </thead>
                                <tbody>
                                    ${dataRow}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>  
            `



            fieldsetContainer=`<div id="danhSachSanPhamChiTiet">${fieldsetContainer}</div>>`

            let imageContainer = `
            <div class="card shadow m-2 w-100 ">
                <div class="card-header py-3">
                    <div class="row">
                        <h1 class="h3 text-gray-800">Chọn tối thiểu 3 hình ảnh tương ứng với màu sắc</h1>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row ms-img">
                        <div class="table-responsive">
                                    <table class="table table-bordered table-data" id="ms-img">
                                        <thead>
                                             <tr>
                                                <th>Màu sắc</th>
                                                <th>Tải lên</th>
                                                <th class="w-75">Chọn hình ảnh</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            ${imageRow}
                                        </tbody>
                                    </table>
                                </div>
                    </div>
                </div>
            </div>
        `

            $('#step-2-content').html(imageContainer + fieldsetContainer)

            //Link sự kiện select all
            $('.table-data .checkAll').off();
            $('.table-data .checkAll').on('click', function () {
                let checked = $(this).prop('checked');
                $(this).closest('table').find('tbody').first().find('input[type="checkbox"]').prop('checked', checked);
            })
            //Link sự kiện delete biến thể
            $('.table-data .delete-spct').off();
            $('.table-data .delete-spct').on('click', function () {
                variantChangeFlagForStep3=true;
                let tbody = $(this).closest('tbody');
                $(this).closest('tr').remove()
                //Đánh lại số thứ tự
                tbody.find('td:nth-child(2)').each((i, e) => {
                    $(e).text(i + 1);
                })
            })
            //Link sự kiện input giá bán
            $('.giaBan-input').off()
            $('.giaBan-input').on('blur', function () {
                let value = $(this).val().replace(/[^0-9]/g, ''); // Loại bỏ tất cả ký tự không phải số
                let formattedValue = numeral(value).format('0,0') + ' VND';
                $(this).val(formattedValue.replace(/,/g, '.'));
            })

            $('.giaBan-input').on('input paste', function () {
                if ($(this).closest('tr').find('input[type="checkbox"]').first().prop('checked') == true) {
                    let value = $(this).val().replace(/[^0-9]/g, ''); // Loại bỏ tất cả ký tự không phải số
                    let formattedValue = numeral(value).format('0,0') + ' VND';
                    console.log('i')
                    $(this).closest('tbody')
                        .find('tr input[type="checkbox"]:checked')
                        .closest('tr')
                        .find('input:last')
                        .not($(this))
                        .val((formattedValue.replace(/,/g, '.')))
                }
            });


            //Link sự kiện delete phiên bản
            $('.btn-delete').off()
            $('.btn-delete').on("click", function () {
                let removeRom = $(this).closest('.card').find('table').prop('id');

                Swal.fire({
                    title: `Bạn chắc chắn muốn xóa phiên bản này?`,
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#3085d6",
                    cancelButtonColor: "#d33",
                    cancelButtonText: "Hủy",
                    confirmButtonText: "Xác nhận"
                }).then((result) => {

                    if (result.isConfirmed) {
                        $(this).closest('.card').remove()
                        let selectedRoms = $('#sp-rom').val();
                        selectedRoms.splice(selectedRoms.indexOf(removeRom), 1);
                        $('#sp-rom').val(selectedRoms).trigger('change')

                        Toast.fire({
                            icon: "success",
                            title: "Xóa phiên bản thành công"
                        })
                        variantChangeFlagForStep3=true;
                    }
                });

            })

            //Uload image
            $('.ms-img input[type="file"]').on('change', function () {
                let formData = new FormData();
                let msId=$(this).prop("ms-id")
                if(this.files.length<3){
                    Toast.fire({
                        icon: "error",
                        title: "Vui lòng chọn tối thiểu 3 hình ảnh về sản phẩm"
                    })
                }else{
                    for (let i = 0; i < this.files.length; i++) {
                        formData.append("image",this.files[i]);
                    }
                    let uload=$(this).closest('tr')
                    $.ajax({
                        url: 'http://localhost:8080/image/upload-temp',  // Thay 'YOUR_API_ENDPOINT' bằng URL của API của bạn
                        type: 'POST',
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (response) {
                            let imgResult="";
                            response.forEach(img=>{
                                imgResult+=`
                                <div class="image-container">
                                    <img src="${img.url}" alt="${img.name}" ms-id="${msId}">
                                    <div class="image-caption">${img.name}</div>
                                </div>
                            `
                            })
                            uload.find(".d-flex").html(imgResult)
                            uload.find("img").first().parent().addClass("bg-gray-400");
                            uload.find("img").on("click",function (){
                                $(this).closest('tr').find(".bg-gray-400").removeClass('bg-gray-400')
                                $(this).parent().addClass("bg-gray-400");
                            })

                            console.log('File uploaded successfully', response);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log('File upload failed', textStatus, errorThrown);
                        }
                    });
                }


            });



        };
        variantChangeFlagForStep2=false;
    })
})

//Step 3
var lstImg=[];
$(document).ready(() => {

    $("a[href='#step-3']").on("click", function () {
        if(variantChangeFlagForStep3){

            let lstRoms=$('#sp-rom').select2('data'),
                lstMauSacs=$('#sp-mau-sac').select2('data'),
                tenSP=$('#sp-ten').val();

            let columnSP='';
            let tableSPKM=""

            let danhSachSanPhamChiTiet=[];
            $('#danhSachSanPhamChiTiet').find('.san-pham-chi-tiet').each(function () {
                danhSachSanPhamChiTiet.push({
                    idMauSac:$(this).find('input[type="text"]').first().attr('ms-id'),
                    tenMauSac:$(this).children('td').eq(2).text(),
                    rom:$(this).closest('table').first().attr('rom-id'),

                })
                
            })
            danhSachSanPhamChiTiet.forEach(e=>{
                columnSP+=`
                    <tr rom-id="${e.rom}" ms-id="${e.idMauSac}">
                       <td style="font-size: 14px"  >${tenSP} ${e.rom} ${e.tenMauSac}</td>
                    </tr>
                        `
            })
            lstRoms.forEach(rom=>{
                lstMauSacs.forEach(mauSac=>{
                    
                })
            })


            $('#tbl-khuyen-mai-ap-dung').find('tbody').html(
                columnSP
            )



            tableSPKM=``
            {
                let tableDanhSach=$('#tbl-danh-sach-khuyen-mai');
                let tableApDung=$('#tbl-khuyen-mai-ap-dung');

                let dataLstKm="";
                let dataLstKhuyenMaiApDun="";
                let count=1;

                //Remove all if not 1st col
                $('#tbl-khuyen-mai-ap-dung').find('tr').each((index,item)=>{
                    $(item).find('td,th').not($(item).find('td,th').first()).remove()
                })

                $('#sp-khuyen-mai').select2('data').forEach(km=>{
                    let ma=new Intl.NumberFormat( 'en-US', {  minimumIntegerDigits: 3, useGrouping: false }
                    ).format(count++)
                    dataLstKm+=`
                <tr>
                    <td km-id="${km.id}">KM${ma}</td>
                    <td>${km.text}</td>
                </tr>
            `;


                    tableApDung.find("thead tr").append(`
                <th class="text-center" km-id="${km.id}">KM${ma}</th>>
            `)
                    tableApDung.find("tbody tr").append(`
                <td class="text-center"><input type="checkbox" checked km-id="${km.id}" data-toggle="tooltip" data-placement="right" title="${km.text}"></td>>
            `)
                })
                tableDanhSach.find("tbody").html(dataLstKm)
            }
        }
        variantChangeFlagForStep3=false;
    })

    $('#sp-khuyen-mai').on('change',function (){

        let tableDanhSach=$(this).closest('.card').find('table').first();
        let tableApDung=$(this).closest('.card').find('table').last();

        let dataLstKm="";
        let dataLstKhuyenMaiApDun="";
        let count=1;

        //Remove all if not 1st col
        $('#tbl-khuyen-mai-ap-dung').find('tr').each((index,item)=>{
            $(item).find('td,th').not($(item).find('td,th').first()).remove()
        })

        $('#sp-khuyen-mai').select2('data').forEach(km=>{
            let ma=new Intl.NumberFormat( 'en-US', {  minimumIntegerDigits: 3, useGrouping: false }
            ).format(count++)
            dataLstKm+=`
                <tr>
                    <td km-id="${km.id}">KM${ma}</td>
                    <td>${km.text}</td>
                </tr>
            `;


            tableApDung.find("thead tr").append(`
                <th class="text-center" km-id="${km.id}">KM${ma}</th>>
            `)
            tableApDung.find("tbody tr").append(`
                <td class="text-center"><input type="checkbox" checked km-id="${km.id}" data-toggle="tooltip" data-placement="right" title="${km.text}"></td>>
            `)
        })
        tableDanhSach.find("tbody").html(dataLstKm)
    })

    {
        tinymce.init({
            selector: 'textarea',
            min_height: 1200,
            plugins: 'anchor autolink link lists image code wordcount',
            toolbar: 'undo redo | blocks | bold italic strikethrough backcolor | link image | align bullist numlist | code ',
            images_upload_handler: function (blobInfo, success, failure) {
                return new Promise(function (resolve, reject) {
                    var formData = new FormData();
                    formData.append('image', blobInfo.blob(), blobInfo.filename());

                    $.ajax({
                        url: '/image/upload-temp',
                        type: 'POST',
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            if (!response || typeof response[0].url !== 'string') {
                                reject('Invalid JSON: ' + JSON.stringify(response));
                                return;
                            }
                            lstImg.push(response[0].name)
                            console.log('uload ss');
                            resolve(response[0].url);
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            reject('HTTP Error: ' + textStatus);
                        }
                    });
                })
            }
        });

    }



});

// Final submit
$(document).ready(function () {
    $('#form-sp').on('submit',function (event){
        event.preventDefault(); // Ngăn chặn submit mặc định của form
        const form = $(this);
        if (!form[0].checkValidity()) {
            // Nếu form không hợp lệ, hiển thị các lỗi validation từ Bootstrap
            event.stopPropagation();
            form.addClass('was-validated');
        }
            else {

            let sp = {
                "id": null,
                "anhName": "",
                "tenSanPham": "",
                "manHinhCongNgheManHinhId": "",
                "manHinhDoPhanGiai": "",
                "manHinhManHinhRong": "",
                "manHinhDoSangToiDa": "",
                "manHinhMatKinhCamUngId": "",
                "cameraTruocDoPhanGiai": "",
                "cameraTruocTinhNangCameraIds": "",
                "cameraSauDoPhanGiai": "",
                "cameraSauDenFlash": "",
                "cameraSauTinhNangCameraIds": "",
                "heDieuHanhVaCpuHeDieuHanhId": "",
                "heDieuHanhVaCpuCpuId": "",
                "ketNoiMangDiDong": "",
                "ketNoiSim": "",
                "ketNoiWifiIds": "",
                "ketNoiGpsIds": "",
                "ketNoiBluetoothIds": "",
                "ketNoiCongSac": "",
                "ketNoiJackTaiNghe": "",
                "pinVaSacDungLuongPin": "",
                "pinVaSacLoaiPin": "",
                "pinVaSacHoTroSacToiDa": "",
                "pinVaSacCongNghePinIds": "",
                "thongTinChungThietKe": "",
                "thongTinChungMauSac": "",
                "thongTinChungKichThuocKhoiLuong": "",
                "thongTinChungTinhNangDacBietIds": "",
                "khuyenMaiIds": "",
                "seriesId": "",
                "sanPhamChiTiet": [
                    {
                        "id": "",
                        "mauSacId": "",
                        "rom": "",
                        "giaBan": "",
                        "giaVon": ""
                    }
                ],
                "ram": "",
                "thoiGianBaoHanh": "",
                "trangThai": "",
                "moTa": "",
                "stt": ""
            }
            sp.anhName = $('#sp-img').val();
            sp.tenSanPham = $('#sp-ten').val();
            sp.trangThai=$('#sp-trang-thai').val();

            sp.manHinhCongNgheManHinhId = $('#sp-cong-nghe-man-hinh').val();
            sp.manHinhDoPhanGiai = $('#sp-doPhanGiai').val();
            sp.manHinhManHinhRong = $('#sp-manHinhRong').val();
            sp.manHinhDoSangToiDa = $('#sp-doSangToiDa').val();
            sp.manHinhMatKinhCamUngId = $('#sp-matKinhCamUng').val();

            sp.cameraTruocDoPhanGiai = $('#sp-cameraTruoc-doPhanGiai').val();
            sp.cameraTruocTinhNangCameraIds = $('#sp-tinhNangCameraTruoc').val();

            sp.cameraSauDoPhanGiai = $('#sp-cameraSau-doPhanGiai').val();
            sp.cameraSauDenFlash = $('#sp-cameraSau-denFlash').val();
            sp.cameraSauTinhNangCameraIds = $('#sp-tinhNangCameraSau').val();

            sp.heDieuHanhVaCpuHeDieuHanhId = $('#sp-heDieuHanh').val();
            sp.heDieuHanhVaCpuCpuId = $('#sp-cpu').val();

            sp.ketNoiMangDiDong = $('#sp-ketNoi-mangDiDong').val();
            sp.ketNoiSim = $('#sp-ketNoi-sim').val();
            sp.ketNoiWifiIds = $('#sp-ketNoi-wifi').val();
            sp.ketNoiGpsIds = $('#sp-ketNoi-gps').val();
            sp.ketNoiBluetoothIds = $('#sp-ketNoi-bluetooth').val();
            sp.ketNoiCongSac = $('#sp-ketNoi-sac').val();
            sp.ketNoiJackTaiNghe = $('#sp-ketNoi-taiNghe').val();

            sp.pinVaSacDungLuongPin = $('#sp-pin-dungLuong').val();
            sp.pinVaSacLoaiPin = $('#sp-pin-loaiPin').val();
            sp.pinVaSacHoTroSacToiDa = $('#sp-pin-sacToiDa').val();
            sp.pinVaSacCongNghePinIds = $('#sp-pin-congNghePin').val();

            sp.thongTinChungThietKe = $('#sp-ttc-thietKe').val();
            sp.thongTinChungMauSac = $('#sp-ttc-chatLieu').val();
            sp.thongTinChungKichThuocKhoiLuong = $('#sp-ttc-kichThuocKhoiLuong').val();
            sp.thoiGianBaoHanh = $('#sp-ttc-thoiGianBaoHanh').val();
            sp.thongTinChungTinhNangDacBietIds = $('#sp-ttc-tinhNangDacBiet').val();
            sp.seriesId = $('#sp-series').val();


            sp.ram = $('#sp-ram').val();
            sp.thoiGianBaoHanh = $('#sp-ttc-thoiGianBaoHanh').val();

            //Danh lấy danh sách ảnh theo màu sắc
            let danhSachAnhTheoMauSac = new Map();
            $('#ms-img').find('tbody input[type="file"]').each((index, inputFile) => {
                    let lstImgName = []
                    $(inputFile).closest('tr').find('img').each((i, img) => {
                        if ($(img).parent().hasClass("bg-gray-400"))
                            lstImgName.unshift($(img).attr('alt'));
                        else
                            lstImgName.push($(img).attr('alt'));
                    })
                    danhSachAnhTheoMauSac.set(
                        parseInt($(inputFile).attr('ms-id')),
                        lstImgName
                    )

                    $(inputFile).attr('ms-id')
                }
            )

            let sanPhamChiTiet;

            let danhSachSanPhamChiTiet = [];
            let tbodyTblKhuyenMaiApDung = $('#tbl-khuyen-mai-ap-dung tbody').first();

            $('#danhSachSanPhamChiTiet').find('.san-pham-chi-tiet').each(function () {
                let msId = parseInt($(this).find('input[type="text"]').first().attr('ms-id')),
                    rom = $(this).closest('table').first().attr('rom-id'),
                    giaNhap = $(this).find('input[type="text"]').first().val().replace(/\D/g, ""),
                    giaBan = $(this).find('input[type="text"]').last().val().replace(/\D/g, ""),
                    trangThai=$(this).find('select').val(),
                    khuyenMai = [];
                tbodyTblKhuyenMaiApDung.find(`tr[ms-id='${msId}'][rom-id='${rom}']`).find('input:checked').each((index, inputChecked) => {
                    khuyenMai.push($(inputChecked).attr('km-id'));
                })

                danhSachSanPhamChiTiet.push({
                    mauSacId: msId,
                    rom: rom,
                    giaNhap: giaNhap,
                    trangThai: trangThai,
                    giaBan: giaBan,
                    khuyenMaiIds: khuyenMai,
                    anh: danhSachAnhTheoMauSac.get(msId)
                })

            })

            sp.sanPhamChiTiet = danhSachSanPhamChiTiet

            sp.moTa = tinymce.get('sp-mo-ta').getContent()


            console.log(sp)

            // Nếu form hợp lệ, gửi dữ liệu form lên server
            $.ajax({
                url: "/api/v1/san-pham",
                method: 'PUT', // Phương thức HTTP
                data: JSON.stringify(sp),
                contentType: 'application/json',
                success: function (response) {
                    Toast.fire({
                        icon: "success",
                        title: "Thêm mới thành công"
                    })
                    console.log(response);
                },
                error: function (xhr, status, error) {
                    Toast.fire({
                        icon: "error",
                        title: "Thêm mới thất bại"
                    });
                    console.log(response);
                    console.error(xhr.responseText);
                }
            });
        }
    })


})



