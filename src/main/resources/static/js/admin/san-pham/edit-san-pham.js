var urlAPI = "/api/v1/admin/data-list-add-san-pham";


var dataList = {}
var variantChangeFlagForStep2 = true;
var variantChangeFlagForStep3 = true;
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
            url: '/image/upload-temp',  // Thay 'YOUR_API_ENDPOINT' bằng URL của API của bạn
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                $('#sp-img').val(response[0].url);
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
    $("#sp-ma").on('input paste', function () {
        variantChangeFlagForStep2 = true;// Thay đổi biến thể
        if (initFlag) {
            initFlag = false;
            exitstName = dataList.code.map(name => name.toLowerCase().trim().replace(/\s/g, ''));
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
// $("#overlay").show();
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
        $("#sp-mauSac").select2({
            data: dataList.mauSac,
            placeholder: "Chọn màu sắc",
            closeOnSelect: false
        });

        $("#sp-chatLieu").select2({
            data: dataList.chatLieu,
            placeholder: "Chọn chất liệu"
        });
        $("#sp-danhMuc").select2({
            data: dataList.danhMuc,
            placeholder: "Chọn danh mục",
            closeOnSelect: false

        });
        $("#sp-kichThuoc").select2({
            data: dataList.kichThuoc,
            placeholder: "Chọn kích thước",
            closeOnSelect: false

        });
        $("#sp-kieuDang").select2({
            data: dataList.kieuDang,
            placeholder: "Chọn kiểu dáng"
        });
        $("#sp-thuongHieu").select2({
            data: dataList.thuongHieu,
            placeholder: "Chọn thương hiệu"
        });

        // Ẩn overlay và hiện sugget
        $("#overlay").hide();


        $('#sp-kichThuoc,#sp-mau-sac,#sp-ten').on('change', function () {
            variantChangeFlagForStep2 = true;
            variantChangeFlagForStep3 = true;
        })

        $("#overlay").show();

        const urlPath = window.location.pathname;
        const parts = urlPath.split("/");
        const id = parts[parts.length - 1];
        $.ajax({
            url: "/api/v1/admin/data-list-add-san-pham/" + id,
            contentType: 'application/json',
            success: function (response) {
                $("#overlay").hide();
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
        }, function (start, end, label) {
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



})
var dataTemp;
//Đẩy data lên form
const fillData = (spTemp) => {
    dataTemp = {
        thuongHieuId: 2,
        chatLieuId: 3,
        kieuDangId: 4,
        maSanPham: "SP004",
        ten: "Váy hoa nữ",
        trangThai: "Còn hàng",
        moTa: "Váy hoa voan",
        anhUrl: "vay_hoa.jpg",
        isDeleted: false,
        sanPhamChiTiets: [
            {
                kichThuocId: 2,
                mauSacId: 4,
                giaBan: 350000.00,
                soLuong: 30,
                barcode: "barcode_4",
                isDeleted: false
            }
        ],
        danhMucIds: null,
        anhs: [
            {
                isDefault: true,
                url: "anh_vay_trang.jpg",
                mauSacId: 3
            }
        ]
    }
    dataTemp = spTemp;
    console.log(dataTemp)
    $('#sp-thuongHieu').val(dataTemp.thuongHieuId).trigger('change');
    $('#sp-chatLieu').val(dataTemp.chatLieuId).trigger('change');
    $('#sp-danhMuc').val(dataTemp.danhMucIds).trigger('change');
    $('#sp-kieuDang').val(dataTemp.kieuDangId).trigger('change');
    $('#sp-ten').val(dataTemp.ten).trigger('change');
    $('#sp-moTa').val(dataTemp.moTa).trigger('change');
    let msIds = dataTemp.sanPhamChiTiets.map(spct => spct.mauSacId);
    let ktIds = dataTemp.sanPhamChiTiets.map(spct => spct.kichThuocId);
    $('#sp-mauSac').val(msIds).trigger('change');
    $('#sp-kichThuoc').val(ktIds).trigger('change');
    $('#sp-ma').val(dataTemp.maSanPham)
    $('#imagePreview').css('background-image', `url(${dataTemp.anhUrl})`);
    fillDataStep2()
    variantChangeFlagForStep2 = false;
    fillValueStep2()
}

function fillValueStep2() {

    // Fill to img
    {
        let mapImg=new Map();
        dataTemp.anhs.forEach(img => {


            let imgResult = `
                                <div class="image-container ${img.isDefault?'bg-gray-400':''}">
                                    <img src="${img.url}" alt="${img.name}" ms-id="${img.mauSacId}">
                                    <div class="image-caption"></div>
                                </div>
                            `
            mapImg.set(img.mauSacId, (mapImg.get(img.mauSacId) || "") + imgResult);



        })
        mapImg.forEach((val,key)=>{
            $(`#img-${key}`).closest('tr').find('.d-flex').html(val)
            $(`#img-${key}`).closest('tr').find("img").on("click", function () {
                $(this).closest('tr').find(".bg-gray-400").removeClass('bg-gray-400')
                $(this).parent().addClass("bg-gray-400");
            })
        })
    }
    //Fill to variant
    dataTemp.sanPhamChiTiets.forEach(spct=>{
        let $row=$(`table[kt-id=${spct.kichThuocId}] input[ms-id=${spct.mauSacId}]`).closest('tr')
        $row.find('input[type="text"]').val(spct.giaBan).trigger('change')
        $row.find('input[type="number"]').eq(0).val(spct.soLuong).trigger('change')
        $row.find('input[type="number"]').eq(1).val(spct.barcode).trigger('change')
        if (spct.isDeleted){
            $row.find('input,button').prop("disabled", true);
        }
    })
}

$(document).ready(() => {
    $("a[href='#step-2']").on("click", function () {
        if (variantChangeFlagForStep2)
            fillDataStep2();
        variantChangeFlagForStep2 = false;
    })
})
let fillDataStep2=function () {
    let fieldsetContainer = '';
    let dataRow = '';
    let imageRow = '';
    let statusRow = '';
    let count = 1;

    //Tạo dòng  giá nhập giá bán input cho từng màu sắc và hình ảnh của chúng
    $('#sp-mauSac').select2('data').forEach(function (mauSac) {
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
                <td><input ms-id="${mauSac.id}" type="checkbox"></td>
                <td>${count++}</td>
                <td>${mauSac.text}<span class="d-none ms-id">${mauSac.id}</span></td>
                <td><input ms-id="${mauSac.id}" class="form-control form-control-sm giaBan-input" type="text" placeholder="Giá bán" aria-label=".form-control-sm example" required></td>
                <td style="display:flex; gap:20px">
                    <input  class="form-control form-control-sm" type="number" min="0" placeholder="Số lượng" aria-label=".form-control-sm example" required>
                    <input  class="form-control form-control-sm" type="number" min="0" placeholder="Barcode" aria-label=".form-control-sm example" required>
                </td>
                
                <td><button type="button" class="btn btn-danger btn-sm  delete-spct">Xóa</button></td>
            </tr>
            `

    })

    //Tạo card input cho từng phiên bản rom
    $('#sp-kichThuoc').val().forEach(elem => {
        fieldsetContainer += `
                    <div class="card shadow m-2 w-100 ">
                            <div class="card-header py-3">
                                <div class="row">
                                    <h1 class="h3 text-gray-800 col-4">Size ${$('#sp-kichThuoc').find(`[value=${elem}]`).text()}</h1>
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
                                    <table class="table table-bordered table-data" kt-id="${elem}">
                                        <thead>
                                        <tr>
                                            <th><input type="checkbox" class="checkAll"></th>
                                            <th>Số thứ tự</th>
                                            <th>Màu sắc</th>
                                            <th>Giá bán</th>
                                            <th>Số lượng/Barcode</th>
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


    let tenSanPham = $('#sp-ten').val();
    let statusContainer = `
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


    fieldsetContainer = `<div id="danhSachSanPhamChiTiet">${fieldsetContainer}</div>>`

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
        variantChangeFlagForStep3 = true;
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
            $(this).closest('#danhSachSanPhamChiTiet')
                .find('tbody tr input[type="checkbox"]:checked')
                .closest('tr')
                .find('input[type="text"]')
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
                let selectedRoms = $('#sp-kichThuoc').val();
                selectedRoms.splice(selectedRoms.indexOf(removeRom), 1);
                $('#sp-kichThuoc').val(selectedRoms).trigger('change')

                Toast.fire({
                    icon: "success",
                    title: "Xóa phiên bản thành công"
                })
                variantChangeFlagForStep3 = true;
            }
        });

    })

    //Uload image
    $('.ms-img input[type="file"]').on('change', function () {
        let formData = new FormData();
        let msId = $(this).prop("ms-id")
        if (this.files.length < 3) {
            Toast.fire({
                icon: "error",
                title: "Vui lòng chọn tối thiểu 3 hình ảnh về sản phẩm"
            })
        } else {
            for (let i = 0; i < this.files.length; i++) {
                formData.append("image", this.files[i]);
            }
            let uload = $(this).closest('tr')
            $.ajax({
                url: '/image/upload-temp',  // Thay 'YOUR_API_ENDPOINT' bằng URL của API của bạn
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    let imgResult = "";
                    response.forEach(img => {
                        imgResult += `
                                <div class="image-container">
                                    <img src="${img.url}" alt="${img.name}" ms-id="${msId}">
                                    <div class="image-caption">${img.name}</div>
                                </div>
                            `
                    })
                    uload.find(".d-flex").html(imgResult)
                    uload.find("img").first().parent().addClass("bg-gray-400");
                    uload.find("img").on("click", function () {
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


}

//Step 3
var lstImg = [];
$(document).ready(() => {

    $("a[href='#step-3']").on("click", function () {
        if (variantChangeFlagForStep3) {

            let lstRoms = $('#sp-kichThuoc').select2('data'),
                lstMauSacs = $('#sp-mau-sac').select2('data'),
                tenSP = $('#sp-ten').val();

            let columnSP = '';
            let tableSPKM = ""

            let danhSachSanPhamChiTiet = [];
            $('#danhSachSanPhamChiTiet').find('.san-pham-chi-tiet').each(function () {
                danhSachSanPhamChiTiet.push({
                    idMauSac: $(this).find('input[type="text"]').first().attr('ms-id'),
                    tenMauSac: $(this).children('td').eq(2).text(),
                    rom: $(this).closest('table').first().attr('rom-id'),

                })

            })
            danhSachSanPhamChiTiet.forEach(e => {
                columnSP += `
                    <tr rom-id="${e.rom}" ms-id="${e.idMauSac}">
                       <td style="font-size: 14px"  >${tenSP} ${e.rom} ${e.tenMauSac}</td>
                    </tr>
                        `
            })
            lstRoms.forEach(rom => {
                lstMauSacs.forEach(mauSac => {

                })
            })


            $('#tbl-khuyen-mai-ap-dung').find('tbody').html(
                columnSP
            )


            tableSPKM = ``
            {
                let tableDanhSach = $('#tbl-danh-sach-khuyen-mai');
                let tableApDung = $('#tbl-khuyen-mai-ap-dung');

                let dataLstKm = "";
                let dataLstKhuyenMaiApDun = "";
                let count = 1;

                //Remove all if not 1st col
                $('#tbl-khuyen-mai-ap-dung').find('tr').each((index, item) => {
                    $(item).find('td,th').not($(item).find('td,th').first()).remove()
                })

                $('#sp-khuyen-mai').select2('data').forEach(km => {
                    let ma = new Intl.NumberFormat('en-US', {minimumIntegerDigits: 3, useGrouping: false}
                    ).format(count++)
                    dataLstKm += `
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
        variantChangeFlagForStep3 = false;
    })

    $('#sp-khuyen-mai').on('change', function () {

        let tableDanhSach = $(this).closest('.card').find('table').first();
        let tableApDung = $(this).closest('.card').find('table').last();

        let dataLstKm = "";
        let dataLstKhuyenMaiApDun = "";
        let count = 1;

        //Remove all if not 1st col
        $('#tbl-khuyen-mai-ap-dung').find('tr').each((index, item) => {
            $(item).find('td,th').not($(item).find('td,th').first()).remove()
        })

        $('#sp-khuyen-mai').select2('data').forEach(km => {
            let ma = new Intl.NumberFormat('en-US', {minimumIntegerDigits: 3, useGrouping: false}
            ).format(count++)
            dataLstKm += `
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
                        success: function (response) {
                            if (!response || typeof response[0].url !== 'string') {
                                reject('Invalid JSON: ' + JSON.stringify(response));
                                return;
                            }
                            lstImg.push(response[0].name)
                            console.log('uload ss');
                            resolve(response[0].url);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
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
    $('#form-sp').on('submit', function (event) {
        event.preventDefault();
        const form = $(this);
        if (!form[0].checkValidity()) {
            event.stopPropagation();
            form.addClass('was-validated');
        } else {

            let sp = {
                thuongHieuId: "",
                chatLieuId: "",
                kieuDangId: "",
                maSanPham: "",
                ten: "",
                trangThai: "",
                moTa: "",
                anhUrl: "",
                isDeleted: false,
                sanPhamChiTiets: [
                    {
                        kichThuocId: "",
                        mauSacId: "",
                        giaBan: "",
                        soLuong: "",
                        barcod: "",
                        isDeleted: false
                    }
                ],
                anhs: [
                    {
                        url: "",
                        isDefault: false,
                        mauSacId: ''
                    }
                ],
                danhMucIds: [10, 20, 30]
            }

            sp.ten = $('#sp-ten').val();
            sp.maSanPham = $('#sp-maSanPham').val();
            sp.trangThai = $('#sp-trangThai').val();
            sp.thuongHieuId = $('#sp-thuongHieu').val();
            sp.chatLieuId = $('#sp-chatLieu').val();
            sp.kieuDangId = $('#sp-kieuDang').val();
            sp.maSanPham = $('#sp-ma').val();
            sp.anhUrl = $('#sp-img').val();
            sp.sanPhamChiTiets = $('#danhSachSanPhamChiTiet').find('tbody tr').map((i, e) => {
                let obj = {

                    kichThuocId: $(e).closest('table').eq(0).attr('kt-id'),
                    mauSacId: $(e).find('input[type="text"]').eq(0).attr('ms-id'),
                    giaBan: $(e).find('input[type="text"]').eq(0).val().replace(/\D/g, ''),
                    soLuong: $(e).find('input[type="number"]').eq(0).val().replace(/\D/g, ''),
                    barcode: $(e).find('input[type="number"]').eq(1).val().replace(/\D/g, ''),
                    isDeleted: false
                }
                return obj
            }).get();
            sp.danhMucIds = $('#sp-danhMuc').val();
            //Danh lấy danh sách ảnh theo màu sắc
            let danhSachAnhTheoMauSac = new Map();
            $('#ms-img').find('tbody input[type="file"]').each((index, inputFile) => {
                    let lstImgName = []
                    $(inputFile).closest('tr').find('img').each((i, img) => {
                        if ($(img).parent().hasClass("bg-gray-400"))
                            lstImgName.unshift($(img).attr('src'));
                        else
                            lstImgName.push($(img).attr('src'));
                    })
                    danhSachAnhTheoMauSac.set(
                        parseInt($(inputFile).attr('ms-id')),
                        lstImgName
                    )

                    $(inputFile).attr('ms-id')
                }
            )

            sp.anhs = [...danhSachAnhTheoMauSac.entries()].flatMap(([key, val]) =>
                val.map((url, index) => ({
                    mauSacId: key,
                    url,
                    isDefault: index === 0
                })))


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



