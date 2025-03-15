
function chuyenCheDoNhap(btn) {


    showSuccessToast("Chuyển chế độ nhập", "Đã chuyển sang chế độ nhập " + (status == "false" ? "thường" : "nhanh"))
}
function showSuccessToast(title, message) {
    $('.toast-container').append(`
       <div class="toast hide " role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
            <div class="toast-header bg-primary text-light">
                <strong class="me-auto">${title}</strong>
            </div>
            <div class="toast-body ">
                ${message}
            </div>
        </div>
    `);
    $('.toast-container').find('.toast').last().toast('show')
}

function showErrToast(title, message) {
    $('.toast-container').append(`
       <div class="toast hide " role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
            <div class="toast-header bg-danger text-light">
                <strong class="me-auto">${title}</strong>
            </div>
            <div class="toast-body ">
                ${message}
            </div>
        </div>
    `);
    $('.toast-container').find('.toast').last().toast('show')
}
function showWarnToast(title, message) {
    $('.toast-container').append(`
       <div class="toast hide " role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
            <div class="toast-header bg-warning text-light">
                <strong class="me-auto">${title}</strong>
            </div>
            <div class="toast-body ">
                ${message}
            </div>
        </div>
    `);
    $('.toast-container').find('.toast').last().toast('show')
}


//Chứa danh sách toàn bộ sản phẩm đang trong các hóa đơn;
const danhSachSanPhamDaThem = new Map();
//Chứa danh sách các hóa đơn
var _mapInvoice = new Map();
//Chứa danh sách sản phẩm
var _mapProduct = new Map();
//Chứa danh sách sản phẩm đang được chọn
var _mapProductDetail = new Map();
//Sản phẩm chi tiết đang được chọn
var _selectedId = -1;
//Hóa đơn đang được chọn
var _selectedInvoice = -1;
//Map sản phẩm chi tiết theo kichThuoc làm key
var _uniqueProductMauSac;
//Chứa đuôi của url filter sản phẩm
let _mapParamFilters= new Map();


$(document).ready(() => {
    $('.collapse').collapse()

    function indexInvoice() {
        if ($('#nav-invoice').find('button span').length == 0) {
            $('#add-new-invoice').trigger('click');
        }

        $('#nav-invoice').find('button span').each((index, item) => {
            $(item).text(index + 1)
        })
    }

    function deleteInvoice(invoiceID) {
        let $iv = $(`#nav-invoice [invoice-id='${invoiceID}']`);
        let $closestSibling = $iv.parent().next().find('.fa-x').length != 0 ? $iv.parent().next() : $iv.parent().prev();
        $iv.parent().remove();
        indexInvoice();
        _mapInvoice.delete(invoiceID);
        updateDSSPDC();
        selectInvoice($closestSibling.find('.fa-x').attr('invoice-id'));

    }

    function selectInvoice(invoiceId) {
        let $selectInvoice = $(`#nav-invoice .fa-x[invoice-id='${invoiceId}']`);
        $selectInvoice.parent().parent().find('.active').removeClass('active').attr('aria-selected', 'false');
        $selectInvoice.parent().addClass('active').attr('aria-selected', 'true');
        _selectedInvoice = invoiceId.toString();
        loadInvoice();
    }

    //Tạo id cho hóa đơn đầu tiên
    $('#nav-invoice').find('button .fa-x').first().attr('invoice-id', Date.now())
    _selectedInvoice = $('#nav-invoice').find('button .fa-x').first().attr('invoice-id');
    _mapInvoice.set(_selectedInvoice, new Map());

    $('#nav-invoice').on('click', '.fa-x', function () {
        deleteInvoice($(this).attr('invoice-id'));
    });

    $('#add-new-invoice').on('click', function () {
        let invoice_id = Date.now().toString();

        let newInvoice = $(`
            <button class="nav-link" id="nav-profile-tab" data-toggle="tab" data-target="#nav-profile" type="button" role="tab" aria-controls="nav-profile" aria-selected="false">
                Hóa đơn <span>1</span> <i invoice-id="${invoice_id}" class="fa fa-x ms-2 p-1 rounded-circle" style="font-size: 10px"></i>
            </button>
        `)
        $(this).before(newInvoice);
        _mapInvoice.set(invoice_id, new Map());
        selectInvoice(invoice_id);

        //Xóa hóa đơn
        newInvoice.on('click', '.fa-x', function () {
            deleteInvoice($(this).attr('invoice-id'));
        })
        indexInvoice()
        newInvoice.on('click', function () {
            selectInvoice($(this).find('.fa-x').attr('invoice-id'));
        })
    })
    $('#nav-invoice').find('button.nav-link').on('click', function () {
        selectInvoice($(this).find('.fa-x').attr('invoice-id'));
    })

})
var totalPrice=0;
var maGiamGiaGiam=0;
var selectedKhachHang;
$(document).ready(() => {
    $('#hd-khach-hang').select2({
        placeholder: "Chọn 1 khách hàng",
        ajax: {
            url: '/admin/api/v1/sale/khach-hang',
            delay: 500,
            data: function (params) {
                return {
                    q: params.term,
                    page: params.page || 0,
                    pageSize: 10
                };
            },
            processResults: function (data) {
                return {
                    results: data.content,
                    pagination: {
                        more: !data.last
                    }
                };
            }

        },
        maximumSelectionLength: 1
    });
    $.get('/api/v1/admin/data-list-add-san-pham/thuongHieu').done(dataSeries=>{
        $('#filter-thuongHieu').select2({
            placeholder: "Tất cả",
            data:  dataSeries.results,
            closeOnSelect:false
        });
    })

    $('#select-san-pham').select2({
        placeholder: "Chọn khách hàng (F4)"
    });


})
//Filter
$(document).ready(()=>{
    $.get('/api/san-pham')
        .done((data) => {
            fillDataToTableSanPham(data)

        })


    let timeout;
    $('#input-search').on('input', function () {
        let searchKey = $(this).val();
        clearTimeout(timeout);
        _mapParamFilters.set('ten',searchKey)
        _mapParamFilters.set('page', 0)
        _mapParamFilters.set('pageSize', 10)
        timeout = setTimeout(applyFilter(), 500); // Thay đổi giá trị này đ
    })
    $('#filter-thuongHieu').on('change', function () {
            _mapParamFilters.set('thuongHieuFilter',$(this).val())
            _mapParamFilters.set('pageNo', 0)
            applyFilter()


    })
    $('#filter-order').on('change', function () {
        _mapParamFilters.set('orderBy',$(this).val())
        _mapParamFilters.set('pageNo', 0)

        applyFilter()
    })

})


function applyFilter() {

    // if($('#filter-order').val()!='id:desc'||$('#filter-thuongHieu').val().length>0){
    //     $('#filter-badge').removeClass('d-none')
    // }else{
    //     $('#filter-badge').addClass('d-none')
    // }



    let suffixUrl='';
    _mapParamFilters.forEach((values, key) => {

        if(!values||values.length>0||Number.isInteger(values)){
            if(Array.isArray(values))
                values.forEach(value=>suffixUrl+=`${key}=${value}&`);
            else{
                suffixUrl+=`${key}=${values}&`
            }
        }
    })
    if(suffixUrl.length>0){
        suffixUrl=suffixUrl.substring(0,suffixUrl.length-1);
    }
    $.get(`/api/san-pham?${suffixUrl}`)
        .done((data) => {
            fillDataToTableSanPham(data)
        })
}

$(document).ready(()=>{
    $('button.show-more').on('click', function () {
        _mapParamFilters.set('page',(_mapParamFilters.get('page'))+1|1);
        applyFilter();
    })
})

function fillDataToTableSanPham(data) {
    let sampleData={
        "content": [
            {
                "id": 1,
                "anhUrl": "http://127.0.0.1:10000/devstoreaccount1/temp-image/3ce346b1-3f5e-400e-9705-8936b129241c-Screenshot%20from%202025-03-03%2011-28-38.png",
                "maSanPham": "SP001",
                "ten": "Áo thun nam",
                "soLuong": 1032329,
                "isPromotionProduct": true,
                "trangThai": "Còn hàng",
                "moTa": "Áo thun cotton 100%",
                "isDeleted": false,
                "sanPhamChiTiets": [
                    {
                        "id": 16,
                        "giaBan": 317956.42,
                        "giaChietKhau": null,
                        "soLuong": 711773,
                        "barcode": "5752569028243",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 3,
                            "ten": "L"
                        }
                    },
                    {
                        "id": 15,
                        "giaBan": 832632.71,
                        "giaChietKhau": null,
                        "soLuong": 270820,
                        "barcode": "3658750107556",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 4,
                            "ten": "XL"
                        }
                    },
                    {
                        "id": 1,
                        "giaBan": 150000.00,
                        "giaChietKhau": null,
                        "soLuong": 50,
                        "barcode": "barcode_1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    },
                    {
                        "id": 8,
                        "giaBan": 978283.90,
                        "giaChietKhau": 928283,
                        "soLuong": 49686,
                        "barcode": "6222787199674",
                        "dotGiamGia": {
                            "id": 1,
                            "ten": "Khuyến mãi tháng 1",
                            "ma": "GG001",
                            "moTa": "Giảm giá 10% cho tất cả sản phẩm",
                            "thoiGianBatDau": "2025-03-06T00:00:00",
                            "thoiGianKetThuc": "2025-07-16T23:59:00",
                            "loaiChietKhau": "PHAN_TRAM",
                            "giaTriGiam": 10.00,
                            "giamToiDa": 50000.00,
                            "trangThai": "DANG_DIEN_RA",
                            "isDeleted": false
                        },
                        "isPromotionProduct": true,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [
                            {
                                "id": 1,
                                "ten": "Khuyến mãi tháng 1",
                                "ma": "GG001",
                                "moTa": "Giảm giá 10% cho tất cả sản phẩm",
                                "thoiGianBatDau": "2025-03-06T00:00:00",
                                "thoiGianKetThuc": "2025-07-16T23:59:00",
                                "loaiChietKhau": "PHAN_TRAM",
                                "giaTriGiam": 10.00,
                                "giamToiDa": 50000.00,
                                "trangThai": "DANG_DIEN_RA",
                                "isDeleted": false
                            },
                            {
                                "id": 2,
                                "ten": "Tết Nguyên Đán",
                                "ma": "GG002",
                                "moTa": "Giảm giá 20% cho sản phẩm chọn lọc",
                                "thoiGianBatDau": "2024-02-08T00:00:00",
                                "thoiGianKetThuc": "2024-02-14T23:59:59",
                                "loaiChietKhau": "PHAN_TRAM",
                                "giaTriGiam": 20.00,
                                "giamToiDa": 100000.00,
                                "trangThai": "DA_KET_THUC",
                                "isDeleted": false
                            }
                        ],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    }
                ],
                "danhMucs": [
                    {
                        "id": 1,
                        "ten": "Áo"
                    },
                    {
                        "id": 2,
                        "ten": "Quần"
                    },
                    {
                        "id": 3,
                        "ten": "Váy"
                    }
                ],
                "anhs": [
                    {
                        "id": 1,
                        "mauSacId": 1,
                        "isDefault": true,
                        "url": "anh_ao_do.jpg",
                        "isDeleted": false
                    },
                    {
                        "id": 2,
                        "mauSacId": 2,
                        "isDefault": false,
                        "url": "anh_ao_xanh.jpg",
                        "isDeleted": false
                    }
                ],
                "thuongHieu": {
                    "id": 1,
                    "ten": "Adidas",
                    "thumbnail": "adidas.jpg"
                },
                "chatLieu": {
                    "id": 1,
                    "ten": "Cotton"
                },
                "kieuDang": {
                    "id": 1,
                    "ten": "Áo thun"
                }
            },
            {
                "id": 3,
                "anhUrl": "http://127.0.0.1:10000/devstoreaccount1/temp-image/3ce346b1-3f5e-400e-9705-8936b129241c-Screenshot%20from%202025-03-03%2011-28-38.png",
                "maSanPham": "SP003",
                "ten": "Áo sơ mi nam",
                "soLuong": 2707138,
                "isPromotionProduct": false,
                "trangThai": "Còn hàng",
                "moTa": "Áo sơ mi linen",
                "isDeleted": false,
                "sanPhamChiTiets": [
                    {
                        "id": 20,
                        "giaBan": 936579.55,
                        "giaChietKhau": null,
                        "soLuong": 228747,
                        "barcode": "5310764725612",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 5,
                        "giaBan": 872347.75,
                        "giaChietKhau": null,
                        "soLuong": 397619,
                        "barcode": "2353325431372",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 14,
                        "giaBan": 964454.88,
                        "giaChietKhau": null,
                        "soLuong": 472157,
                        "barcode": "7553674386887",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 4,
                            "ten": "XL"
                        }
                    },
                    {
                        "id": 21,
                        "giaBan": 30376.21,
                        "giaChietKhau": null,
                        "soLuong": 659405,
                        "barcode": "9884121125934",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 2,
                            "ten": "Xanh",
                            "code": "#00FF00"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 22,
                        "giaBan": 473733.10,
                        "giaChietKhau": null,
                        "soLuong": 949185,
                        "barcode": "1680173881822",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 3,
                            "ten": "L"
                        }
                    },
                    {
                        "id": 3,
                        "giaBan": 300000.00,
                        "giaChietKhau": null,
                        "soLuong": 25,
                        "barcode": "barcode_3",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 3,
                            "ten": "Trắng",
                            "code": "#FFFFFF"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    }
                ],
                "danhMucs": [
                    {
                        "id": 2,
                        "ten": "Quần"
                    },
                    {
                        "id": 3,
                        "ten": "Váy"
                    },
                    {
                        "id": 1,
                        "ten": "Áo"
                    }
                ],
                "anhs": [],
                "thuongHieu": {
                    "id": 1,
                    "ten": "Adidas",
                    "thumbnail": "adidas.jpg"
                },
                "chatLieu": {
                    "id": 1,
                    "ten": "Cotton"
                },
                "kieuDang": {
                    "id": 3,
                    "ten": "Áo sơ mi"
                }
            },
            {
                "id": 16,
                "anhUrl": "http://127.0.0.1:10000/devstoreaccount1/temp-image/3ce346b1-3f5e-400e-9705-8936b129241c-Screenshot%20from%202025-03-03%2011-28-38.png",
                "maSanPham": "213",
                "ten": "Áo thun nam",
                "soLuong": 4,
                "isPromotionProduct": false,
                "trangThai": "CON_HANG",
                "moTa": "<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://mcdn.coolmate.me/image/December2024/AKWBNTASLAN_93.jpg\" width=\"1004\" height=\"2067\"></p>",
                "isDeleted": false,
                "sanPhamChiTiets": [
                    {
                        "id": 46,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 47,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 48,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    },
                    {
                        "id": 45,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    }
                ],
                "danhMucs": [
                    {
                        "id": 2,
                        "ten": "Quần"
                    },
                    {
                        "id": 3,
                        "ten": "Váy"
                    },
                    {
                        "id": 1,
                        "ten": "Áo"
                    }
                ],
                "anhs": [
                    {
                        "id": 40,
                        "mauSacId": 1,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b411708a-9e85-49e0-909f-5aa89eec83b8-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 41,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e47e5752-959d-4b2f-9dd7-7a99be17579a-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 42,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/a655b19d-b98f-45f4-ac0a-8c51a1ad8328-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 43,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/f189252e-6517-4afb-9234-2fdf230eee43-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    },
                    {
                        "id": 44,
                        "mauSacId": 4,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b67414f5-87d3-4998-b796-cabf793d720d-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 45,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/80bc0e38-59cd-4341-92eb-e140b51998a1-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 46,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/6d4ffcec-f4c5-45c1-808e-ba3b65b742ec-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 47,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e14e3e32-1258-450d-aafa-72b426b66610-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    }
                ],
                "thuongHieu": {
                    "id": 1,
                    "ten": "Adidas",
                    "thumbnail": "adidas.jpg"
                },
                "chatLieu": {
                    "id": 1,
                    "ten": "Cotton"
                },
                "kieuDang": {
                    "id": 1,
                    "ten": "Áo thun"
                }
            },
            {
                "id": 17,
                "anhUrl": "http://127.0.0.1:10000/devstoreaccount1/temp-image/3ce346b1-3f5e-400e-9705-8936b129241c-Screenshot%20from%202025-03-03%2011-28-38.png",
                "maSanPham": "2",
                "ten": "Áo thun nam",
                "soLuong": 4,
                "isPromotionProduct": false,
                "trangThai": "CON_HANG",
                "moTa": "<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://mcdn.coolmate.me/image/December2024/AKWBNTASLAN_93.jpg\" width=\"1004\" height=\"2067\"></p>",
                "isDeleted": false,
                "sanPhamChiTiets": [
                    {
                        "id": 51,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 49,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    },
                    {
                        "id": 50,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 52,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    }
                ],
                "danhMucs": [
                    {
                        "id": 2,
                        "ten": "Quần"
                    },
                    {
                        "id": 1,
                        "ten": "Áo"
                    },
                    {
                        "id": 3,
                        "ten": "Váy"
                    }
                ],
                "anhs": [
                    {
                        "id": 48,
                        "mauSacId": 1,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b411708a-9e85-49e0-909f-5aa89eec83b8-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 49,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e47e5752-959d-4b2f-9dd7-7a99be17579a-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 50,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/a655b19d-b98f-45f4-ac0a-8c51a1ad8328-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 51,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/f189252e-6517-4afb-9234-2fdf230eee43-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    },
                    {
                        "id": 52,
                        "mauSacId": 4,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b67414f5-87d3-4998-b796-cabf793d720d-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 53,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/80bc0e38-59cd-4341-92eb-e140b51998a1-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 54,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/6d4ffcec-f4c5-45c1-808e-ba3b65b742ec-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 55,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e14e3e32-1258-450d-aafa-72b426b66610-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    }
                ],
                "thuongHieu": {
                    "id": 1,
                    "ten": "Adidas",
                    "thumbnail": "adidas.jpg"
                },
                "chatLieu": {
                    "id": 1,
                    "ten": "Cotton"
                },
                "kieuDang": {
                    "id": 1,
                    "ten": "Áo thun"
                }
            },
            {
                "id": 18,
                "anhUrl": "http://127.0.0.1:10000/devstoreaccount1/temp-image/3ce346b1-3f5e-400e-9705-8936b129241c-Screenshot%20from%202025-03-03%2011-28-38.png",
                "maSanPham": "232",
                "ten": "Áo thun nam",
                "soLuong": 4,
                "isPromotionProduct": false,
                "trangThai": "CON_HANG",
                "moTa": "<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://mcdn.coolmate.me/image/December2024/AKWBNTASLAN_93.jpg\" width=\"1004\" height=\"2067\"></p>",
                "isDeleted": false,
                "sanPhamChiTiets": [
                    {
                        "id": 56,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 53,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 55,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    },
                    {
                        "id": 54,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    }
                ],
                "danhMucs": [
                    {
                        "id": 1,
                        "ten": "Áo"
                    },
                    {
                        "id": 3,
                        "ten": "Váy"
                    },
                    {
                        "id": 2,
                        "ten": "Quần"
                    }
                ],
                "anhs": [
                    {
                        "id": 56,
                        "mauSacId": 1,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b411708a-9e85-49e0-909f-5aa89eec83b8-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 57,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e47e5752-959d-4b2f-9dd7-7a99be17579a-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 58,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/a655b19d-b98f-45f4-ac0a-8c51a1ad8328-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 59,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/f189252e-6517-4afb-9234-2fdf230eee43-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    },
                    {
                        "id": 60,
                        "mauSacId": 4,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b67414f5-87d3-4998-b796-cabf793d720d-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 61,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/80bc0e38-59cd-4341-92eb-e140b51998a1-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 62,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/6d4ffcec-f4c5-45c1-808e-ba3b65b742ec-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 63,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e14e3e32-1258-450d-aafa-72b426b66610-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    }
                ],
                "thuongHieu": {
                    "id": 1,
                    "ten": "Adidas",
                    "thumbnail": "adidas.jpg"
                },
                "chatLieu": {
                    "id": 1,
                    "ten": "Cotton"
                },
                "kieuDang": {
                    "id": 1,
                    "ten": "Áo thun"
                }
            },
            {
                "id": 19,
                "anhUrl": "http://127.0.0.1:10000/devstoreaccount1/temp-image/3ce346b1-3f5e-400e-9705-8936b129241c-Screenshot%20from%202025-03-03%2011-28-38.png",
                "maSanPham": "2232",
                "ten": "Áo thun nam",
                "soLuong": 4,
                "isPromotionProduct": false,
                "trangThai": "CON_HANG",
                "moTa": "<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://mcdn.coolmate.me/image/December2024/AKWBNTASLAN_93.jpg\" width=\"1004\" height=\"2067\"></p>",
                "isDeleted": false,
                "sanPhamChiTiets": [
                    {
                        "id": 59,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    },
                    {
                        "id": 60,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 58,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 57,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    }
                ],
                "danhMucs": [
                    {
                        "id": 1,
                        "ten": "Áo"
                    },
                    {
                        "id": 3,
                        "ten": "Váy"
                    },
                    {
                        "id": 2,
                        "ten": "Quần"
                    }
                ],
                "anhs": [
                    {
                        "id": 64,
                        "mauSacId": 1,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b411708a-9e85-49e0-909f-5aa89eec83b8-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 65,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e47e5752-959d-4b2f-9dd7-7a99be17579a-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 66,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/a655b19d-b98f-45f4-ac0a-8c51a1ad8328-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 67,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/f189252e-6517-4afb-9234-2fdf230eee43-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    },
                    {
                        "id": 68,
                        "mauSacId": 4,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b67414f5-87d3-4998-b796-cabf793d720d-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 69,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/80bc0e38-59cd-4341-92eb-e140b51998a1-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 70,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/6d4ffcec-f4c5-45c1-808e-ba3b65b742ec-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 71,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e14e3e32-1258-450d-aafa-72b426b66610-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    }
                ],
                "thuongHieu": {
                    "id": 1,
                    "ten": "Adidas",
                    "thumbnail": "adidas.jpg"
                },
                "chatLieu": {
                    "id": 1,
                    "ten": "Cotton"
                },
                "kieuDang": {
                    "id": 1,
                    "ten": "Áo thun"
                }
            },
            {
                "id": 20,
                "anhUrl": "http://127.0.0.1:10000/devstoreaccount1/temp-image/3ce346b1-3f5e-400e-9705-8936b129241c-Screenshot%20from%202025-03-03%2011-28-38.png",
                "maSanPham": "22321",
                "ten": "Áo thun nam",
                "soLuong": 4,
                "isPromotionProduct": false,
                "trangThai": "CON_HANG",
                "moTa": "<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://mcdn.coolmate.me/image/December2024/AKWBNTASLAN_93.jpg\" width=\"1004\" height=\"2067\"></p>",
                "isDeleted": false,
                "sanPhamChiTiets": [
                    {
                        "id": 62,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    },
                    {
                        "id": 64,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 1,
                            "ten": "S"
                        }
                    },
                    {
                        "id": 61,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 4,
                            "ten": "Đen",
                            "code": "#000000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    },
                    {
                        "id": 63,
                        "giaBan": 1.00,
                        "giaChietKhau": null,
                        "soLuong": 1,
                        "barcode": "1",
                        "dotGiamGia": null,
                        "isPromotionProduct": false,
                        "isDeleted": false,
                        "mauSac": {
                            "id": 1,
                            "ten": "Đỏ",
                            "code": "#FF0000"
                        },
                        "dotGiamGias": [],
                        "kichThuoc": {
                            "id": 2,
                            "ten": "M"
                        }
                    }
                ],
                "danhMucs": [
                    {
                        "id": 1,
                        "ten": "Áo"
                    },
                    {
                        "id": 3,
                        "ten": "Váy"
                    },
                    {
                        "id": 2,
                        "ten": "Quần"
                    }
                ],
                "anhs": [
                    {
                        "id": 72,
                        "mauSacId": 1,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b411708a-9e85-49e0-909f-5aa89eec83b8-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 73,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e47e5752-959d-4b2f-9dd7-7a99be17579a-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 74,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/a655b19d-b98f-45f4-ac0a-8c51a1ad8328-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 75,
                        "mauSacId": 1,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/f189252e-6517-4afb-9234-2fdf230eee43-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    },
                    {
                        "id": 76,
                        "mauSacId": 4,
                        "isDefault": true,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/b67414f5-87d3-4998-b796-cabf793d720d-Screenshot%20from%202025-03-03%2011-28-38.png",
                        "isDeleted": false
                    },
                    {
                        "id": 77,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/80bc0e38-59cd-4341-92eb-e140b51998a1-Screenshot%20from%202025-03-03%2011-09-01.png",
                        "isDeleted": false
                    },
                    {
                        "id": 78,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/6d4ffcec-f4c5-45c1-808e-ba3b65b742ec-Screenshot%20from%202025-03-03%2010-59-09.png",
                        "isDeleted": false
                    },
                    {
                        "id": 79,
                        "mauSacId": 4,
                        "isDefault": false,
                        "url": "http://127.0.0.1:10000/devstoreaccount1/temp-image/e14e3e32-1258-450d-aafa-72b426b66610-Screenshot%20from%202025-02-28%2017-56-34.png",
                        "isDeleted": false
                    }
                ],
                "thuongHieu": {
                    "id": 1,
                    "ten": "Adidas",
                    "thumbnail": "adidas.jpg"
                },
                "chatLieu": {
                    "id": 1,
                    "ten": "Cotton"
                },
                "kieuDang": {
                    "id": 1,
                    "ten": "Áo thun"
                }
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 7,
            "sort": {
                "sorted": false,
                "unsorted": true,
                "empty": true
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "totalPages": 1,
        "totalElements": 7,
        "last": true,
        "first": true,
        "size": 7,
        "number": 0,
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "numberOfElements": 7,
        "empty": false
    }
    if(data.first)
        _mapProduct.clear();
    data.content.forEach((item) => {
        _mapProduct.set(item.id, item);
        item.sanPhamChiTiets.forEach(spct=>{
            _mapProductDetail.set(spct.id+"",spct)
        })
    })
    if(!data.last){
        $('button.show-more').parent().show()
        $('button.show-more span').text(`${data.totalElements-data.numberOfElements} sản phẩm`);
    }else{
        $('button.show-more').parent().hide()
    }

    let dataSP = '';
    _mapProduct.forEach((sp, id) => {
        let disable = (sp.trangThai != 'CON_HANG') ? "disable" : "";
        let outOfStock = (sp.soLuong < 1) ? "out-of-stock" : "";
        dataSP += `
                <div sp-id="${sp.id}" class="col-4 d-flex justify-content-between product ${outOfStock} ${disable}">
                    <img class="w-25" src="${sp.anhUrl}">
                    <div class="w-75 ms-1">
                        <span style="font-size: 12px">${sp.ten}</span><br>
                        <span style="font-size: 12px">Số lượng: ${sp.soLuong}</span>
                    </div>
                </div>
                `
    })
    let $dataSP = $(dataSP);
    $('#tbl-san-pham').html($dataSP)


    $('#tbl-san-pham').on('click', '.product:not(".disable")', function () {
        loadToOffcanvas($(this).attr('sp-id'));
    })
}

function toCurrency(Num) {
    Num = Num <= 0 ? 0 : Num;
    return (numeral(Num).format('0,0') + ' VND').replace(/,/g, ".")
}

function textDD_MM_YYYYtoDate(stringDate) {
    return new Date(stringDate.split('-').reverse().join('-'));
}
var currentProduct;
function loadToOffcanvas(idSp) {
    idSp = parseInt(idSp)
    let $canvas = $('#sp-add-to-cart');
    $canvas.offcanvas('show');
    $canvas.find('#detail-sp').hide();
    $canvas.find('.spinner-grow').show();
    updateDSSPDC()
    $.get('/api/san-pham/' + idSp).then(function (sanPham) {
        currentProduct=sanPham
        $('#sp-add-to-cartLabel').text(sanPham.ten)
        let danhSachMauSac=new Map();
            sanPham.sanPhamChiTiets.forEach(spct=>
                danhSachMauSac.set(spct.mauSac.id,spct.mauSac.ten)
            )
        let romBtnHtml=''
        danhSachMauSac.forEach((name, id) => {
            romBtnHtml += `
                <input type="radio" class="btn-check" ms-id="${id}" id="ms-${id}" name="selected-mauSac" autocomplete="off"/>
                <label class="btn btn-secondary" for="ms-${id}">${name}</label>
                          `
        })
        $('#container-btn-mauSac').find('.btn-group').html(romBtnHtml)
        $('#container-btn-mauSac').on('click', 'input', function () {
            let btnKichThuoc = '';
            let mauSacId = $(this).attr('ms-id');
            
            let imgSP=currentProduct.anhs.find(img=>img.mauSacId==mauSacId&img.isDefault)?.url||currentProduct.anhUrl
            $('.offcanvas-body img:first-child').attr('src',imgSP)
            currentProduct.sanPhamChiTiets.filter(spct=>spct.mauSac.id==mauSacId).forEach(
                spct=>{
                    btnKichThuoc += `
                        <input type="radio" class="btn-check" id="spct-${spct.id}" kt-id="${spct.kichThuoc.id}" spct-id='${spct.id}' name="selected-kichThuoc" kt-id="${spct.mauSac.id}" autocomplete="off"/>
                        <label for="spct-${spct.id}" class="btn btn-secondary">${spct.kichThuoc.ten}</label>
                `
                }
            )
            $('#container-btn-kichThuoc').find('.btn-group').html(btnKichThuoc);
            $('#container-btn-kichThuoc').on('click', 'input', function () {

                let sanPhamChiTietId=$(this).attr('spct-id')
                let spct=currentProduct.sanPhamChiTiets.find(spct=>spct.id==sanPhamChiTietId)
                {
                    let giaSanPhamHtml = '';
                    if(spct.isPromotionProduct){
                        if(spct.dotGiamGia.loaiChietKhau == 'PHAN_TRAM')
                            giaSanPhamHtml = `
                                <div class="p-0 d-flex align-items-end">
                                <h5 class="me-3">Giá gốc:</h5><del class="h5 mt-2 price text-15 text-th">${toCurrency(spct.giaBan)}
                                    
                                </del>
                                <span class="ml-1"> - ${ spct.dotGiamGia.giaTriGiam + '%'}</span>
                                
                                </div>
                                 <div class="p-0 d-flex align-items-end">
                                    <h5 class="me-3 p-0 m-0">Giá khuyến mại:</h5><div class="p-0"><span class="h5 mt-2 price font-weight-bold">${toCurrency(spct.giaChietKhau)}</span></div>
                                </div>
                                 `
                        else
                            giaSanPhamHtml = `
                                <div class="p-0 d-flex align-items-end">
                                <h5 class="me-3">Giá gốc:</h5><del class="h5 mt-2 price text-15 text-th">${toCurrency(spct.giaBan)}
                                    
                                </del>
                                <span class="ml-1"> - ${toCurrency(spct.dotGiamGia.giaTriGiam)}</span>
                                
                                </div>
                                 <div class="p-0 d-flex align-items-end">
                                    <h5 class="me-3 p-0 m-0">Giá khuyến mại:</h5><div class="p-0"><span class="h5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan - spct.dotGiamGia.giaTriGiam)}</span></div>
                                </div>
                                 `
                    }else{
                        giaSanPhamHtml = `
                            <div class="p-0 d-flex align-items-end">
                           
                             <div class="p-0 d-flex align-items-end">
                                <h5 class="me-3 p-0 m-0">Giá:</h5><div class="p-0"><span class="h5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                            </div>`
                    }



                    $('#sp-gia').html(giaSanPhamHtml)
                }

                _selectedId = $(this).attr('spct-id');
                $('#sp-add-to-cart').find('[name="so-luong"]').html("Số lượng sản phẩm: <span></span>")
                $canvas.find('[name="so-luong"] span').text(_mapProductDetail.get(_selectedId.toString()).soLuong)
                console.log('selected id' + _selectedId);
            })

        });
        $('#container-btn-mauSac input').eq(0).trigger('click')


        $canvas.find('.spinner-grow').hide();
        $canvas.find('#detail-sp').show();
        $('#container-btn-kichThuoc input').eq(0).trigger('click')
    })
    $canvas.find('.offcanvas-title').text(_mapProduct.get(idSp).tenSanPham)
    $canvas.find('[name="so-luong"] span').text(_mapProduct.get(idSp).soLuong)
}

function deleteInvoice(invoiceId) {
    _mapInvoice.delete(invoiceId)

}

function loadInvoice() {
    let dataRow = '';
    let count = 1;
    _mapInvoice.forEach((value, key) => {
        if ($(`#nav-invoice [invoice-id='${key}']`).length == 0) {
            deleteInvoice(key)
        }
    })
    let revertMap = new Map([...(_mapInvoice.get(_selectedInvoice) || [])].reverse())
    revertMap.forEach((spct, spctId) => {
        dataRow += `
            <tr class="bg-white rounded-lg">
                <td>${count++}</td>
                <td><i spct-id="${spctId}" class="fa-solid fa-trash-can"></i></i></td>
                <td>${spct.sanPhamTen}</td>
                <td><i spct-id="${spctId}" class="fa-solid fa-minus"></i> ${spct.soLuong} <i spct-id="${spctId}" class="fa-solid fa-plus"></i></td>
                <td>${toCurrency(spct.giaBan)}</td>
                <td>${toCurrency(spct.giaBan * spct.soLuong)}</td>
            </tr>
        `
    })
    $('#tbl-invoice').find('tbody').html(dataRow)

    $('#tbl-invoice').find('.fa-plus').on('click', function () {
        let spctId=$(this).attr('spct-id');
        addToCart(spctId,1,"")
    })
    $('#tbl-invoice').find('.fa-minus').on('click', function () {
        let spctId = $(this).attr('spct-id');
        removeFromCart(spctId,-1,"")
    })



    $('#tbl-invoice').find('.fa-trash-can').on('click', function () {
        let spctId = $(this).attr('spct-id');
        _mapInvoice.get(_selectedInvoice).delete(spctId);
        let soLuong = parseInt($(this).closest('tr').find(':nth-child(4)').text());
        danhSachSanPhamDaThem.set(spctId, danhSachSanPhamDaThem.get(spctId) - soLuong)
        this.closest('tr').remove();
    })

}


function updateDSSPDC() {
    danhSachSanPhamDaThem.clear();
    _mapInvoice.forEach((details, id) => {
        details.forEach(detail => {
            if (danhSachSanPhamDaThem.has(detail.sanPhamChiTietId)) {
                let sl = (danhSachSanPhamDaThem.get(detail.sanPhamChiTietId) + detail.soLuong);
                danhSachSanPhamDaThem.set(detail.sanPhamChiTietId, sl);
            }
            else
            danhSachSanPhamDaThem.set(detail.sanPhamChiTietId, detail.soLuong);
        })
    })
}

function removeFromCart(spctId, soLuongThem, tenSanPham) {

    let mapSpct = _mapInvoice.get(_selectedInvoice);
    if (danhSachSanPhamDaThem.get(spctId)) {
        danhSachSanPhamDaThem.set(spctId, (danhSachSanPhamDaThem.get(spctId) + soLuongThem));
    }

    _mapProductDetail.get(spctId).soLuong -= soLuongThem;

    //Kiểm tra tồn tại sản phẩm
    if (mapSpct.get(spctId)) {
        let spTemp = mapSpct.get(spctId);
        spTemp.soLuong += soLuongThem;
        mapSpct.set(spctId, spTemp);
    }
    if(mapSpct.get(spctId).soLuong<1){
        mapSpct.delete(spctId);
    }
    $('#sp-add-to-cart').find('[name="so-luong"]').html("Số lượng sản phẩm: <span></span>")
    $('#sp-add-to-cart').find('[name="so-luong"] span').text(_mapProductDetail.get(spctId).soLuong)
    _mapInvoice.set(_selectedInvoice, mapSpct);
    console.log(_mapInvoice);
    loadInvoice();
}


function addToCart(spctId, soLuongThem, tenSanPham) {
    //Lấy danh sách các sản phẩm của hóa đơn hiện tại
    let mapSpct = _mapInvoice.get(_selectedInvoice);
    //Kiểm tra tồn tại danh sách sản phẩm
    if (mapSpct) {
    } else {
        mapSpct = new Map();
    }
    if (_mapProductDetail.get(spctId).soLuong < soLuongThem) {
        showWarnToast("Lỗi", "Đã vượt quá số lượng tồn kho bạn cần bổ sung sản phẩm này");
    }
    if (danhSachSanPhamDaThem.get(spctId)) {
        danhSachSanPhamDaThem.set(spctId, (danhSachSanPhamDaThem.get(spctId) + soLuongThem));
    } else {
        danhSachSanPhamDaThem.set(spctId, soLuongThem);
    }

    _mapProductDetail.get(spctId).soLuong -= soLuongThem;

    //Kiểm tra tồn tại sản phẩm
    if (mapSpct.get(spctId)) {
        let spTemp = mapSpct.get(spctId);

        spTemp.soLuong += soLuongThem;


        mapSpct.set(spctId, spTemp);
    } else {
        let spct = _mapProductDetail.get(spctId);
        
        mapSpct.set(spctId, {
            sanPhamChiTietId: spctId,
            soLuong: soLuongThem,
            sanPhamTen: tenSanPham + ` ` + spct.mauSac.ten + " " + spct.kichThuoc.ten,
            giaBan: spct.giaChietKhau,
            spct:spct
        });
    }

    $('#sp-add-to-cart').find('[name="so-luong"]').html("Số lượng sản phẩm: <span></span>")
    $('#sp-add-to-cart').find('[name="so-luong"] span').text(_mapProductDetail.get(spctId).soLuong)
    _mapInvoice.set(_selectedInvoice, mapSpct);
    console.log(_mapInvoice);
    loadInvoice();
}

$(document).ready(() => {
    $('#add-to-cart').on('click', function () {
        addToCart(_selectedId, 1, $('#sp-add-to-cartLabel').text());
    })
})


$(document).ready(() => {
    $('#check-out').on('click', () => {
        checkout(_mapInvoice.get(_selectedInvoice));
    })
})
function currencyToNumb(currency){
    return parseInt(currency.replace(/[^0-9]/g, ''))??0;
}
let currentPhieuGiamGia=null;
let currentPhieuGiamGias=null;
function checkout(invoice) {
    if (invoice.size == 0) {
        showErrToast("Lỗi", "Vui lòng chọn hóa đơn có sản phẩm")
    }
    $offcv = $('#offcanvas-check-out');
    $offcv.find('.is-valid,.is-invalid').removeClass('is-invalid').removeClass('is-valid');
    $offcv.offcanvas('show');
    let tongSoSanPham = 0;
    let tongTien = 0;
    let imeiRow = ''
    let tenHoaDon = $(`#nav-invoice [invoice-id='${_selectedInvoice}']`).parent().text();
    invoice.forEach((element) => {
        let gia=toCurrency(element.giaBan*element.soLuong)
        imeiRow += `
        <tr>
            <td>${element.sanPhamChiTietId}</td>
            <td>${element.soLuong}</td>
            <td>${gia}</td>
        </tr>
                
        
        `
        tongSoSanPham += element.soLuong;
        tongTien += element.soLuong * element.giaBan;
    })

    $offcv.find('.offcanvas-title').text(tenHoaDon)
    $('#hd-giam-gia').text('')
    $('#hd-tong-so-luong').text(tongSoSanPham);
    $('#tbl-imei').html(imeiRow);
    $('#hd-tong-so-tien').text(toCurrency(tongTien));
    $('#hd-khach-tra').text(toCurrency(tongTien));



    updateBillStep2(tongTien);
    totalPrice=tongTien;
    $('#hd-voucher').val([]).trigger('change');

    $offcv.find('input[spct-id]').on('focus', function ()  {
        $(this)[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
    })

    $('#hd-voucher').on('select2:select select2:unselect', function () {
        if($(this).val().length==0){
            currentPhieuGiamGia=null;
            applyVoucher(undefined);
            return;
        }else{
            currentPhieuGiamGia=currentPhieuGiamGias.filter(pgg=>pgg.id==$(this).val()[0])[0]
            applyVoucher(currentPhieuGiamGia)
        }

    })



    let showVoucher = (response) => {
        if (response) {
            $('#hd-pgg-ma').val(response.ma);
            $('#hd-pgg-loaiHinhGiam').val(response.loaiHinhGiam=='PHAN_TRAM'?'Phần trần':'Số tiền');
            $('#hd-pgg-hoaDonToiThieu').val(toCurrency(response.hoaDonToiThieu));
            $('#hd-pgg-ngayBatDau').val(response.ngayBatDau);
            $('#hd-pgg-ngayKetThuc').val(response.ngayKetThuc);
            if(response.loaiHinhGiam=='PHAN_TRAM'){
               $('#hd-pgg-giamToiDa').val(toCurrency(response.giamToiDa)).show();
               $('#hd-pgg-giamToiDa').parent().show();
               $('#hd-pgg-giaTriGiam').val(response.giaTriGiam+'%');
           }else{
                $('#hd-pgg-giaTriGiam').val(toCurrency(response.giaTriGiam));
                $('#hd-pgg-giamToiDa').val(toCurrency(response.giamToiDa));
                $('#hd-pgg-giamToiDa').val(toCurrency(response.giamToiDa));
                $('#hd-pgg-giamToiDa').parent().hide();

            }

        }else {
            $('#hd-pgg-ma').val('');
            $('#hd-pgg-loaiHinhGiam').val('');
            $('#hd-pgg-hoaDonToiThieu').val('');
            $('#hd-pgg-ngayBatDau').val('');
            $('#hd-pgg-ngayKetThuc').val('');
            $('#hd-pgg-giamToiDa').val('');
            $('#hd-pgg-giaTriGiam').val('');
        }

    }
    function applyVoucher(response) {
        showVoucher(response)
        if(!response){
            $('#hd-giam-gia').text(toCurrency(0))
            $('#hd-khach-tra').text(toCurrency(tongTien));
            updateBillStep2(tongTien);
            return;
        }

        let voucher = {
            "id": "1",
            "ma": "PGG001",
            "ten": "Phiếu giảm giá 50k",
            "soLuong": 100,
            "hoaDonToiThieu": 500000,
            "loaiHinhGiam": "TUY_CHON",
            "giaTriGiam": 50000,
            "giamToiDa": 50000,
            "ngayBatDau": "2025-03-15T04:03:34",
            "ngayKetThuc": "2025-08-15T04:03:34",
            "trangThai": "Đã kết thúc",
            "isDeleted": false,
            "text": "PGG001"
        }
        voucher = response;


        function getGiaTriGiam(tongTien) {
            if (voucher.loaiHinhGiam === "PHAN_TRAM") {
                return tongTien * voucher.giaTriGiam / 100 > voucher.giamToiDa ? voucher.giamToiDa : tongTien * voucher.giaTriGiam /100

            }else{
                return voucher.giaTriGiam > tongTien?tongTien : voucher.giaTriGiam;

            }
        }

        $('#hd-giam-gia').text(toCurrency(getGiaTriGiam(tongTien)))
        $('#hd-khach-tra').text(toCurrency(tongTien - getGiaTriGiam(tongTien)));
        updateBillStep2(tongTien,getGiaTriGiam(tongTien));
    }
}

$(document).ready(function () {
    function showCustomerDetailInOder(customer) {
        if(customer){
            if((!customer.taiKhoanIsEnable)||customer.isDeleted){
                showWarnToast('Cảnh báo','Tài khoản này đã bị khóa');
                return;
            }
            $('#hd-kh-ten').val(customer.ten).prop("readonly",true);
            $('#hd-kh-email').val(customer.taiKhoanEmail).prop("readonly",true);
            $('#hd-kh-sdt').val(customer.taiKhoanSoDienThoai).prop("readonly",true);
        }else{
            $('#hd-kh-ten').val("").prop("readonly",false);
            $('#hd-kh-email').val("").prop("readonly",false);
            $('#hd-kh-sdt').val("").prop("readonly",false);

        }

    }

    $('#hd-khach-hang').on('select2:select select2:unselect', function () {
        $.get('/admin/api/v1/sale/customer/' + $(this).val())
            .then((response) => {
                showCustomerDetailInOder(response);
                selectedKhachHang=response;
                updateVoucherSelect()
            })
            .catch(()=>{
                showCustomerDetailInOder();
                selectedKhachHang=null;
                updateVoucherSelect()
            })
    })
})
function updateVoucherSelect(){
    console.log(selectedKhachHang)
    $('#hd-voucher').select2({
        placeholder: "Chọn mã giảm giá",
        ajax: {
            url: `/admin/api/v1/sale/promotion?price=${totalPrice}${selectedKhachHang?("&idKH="+selectedKhachHang.id):""}`,
            delay: 500,
            data: function (params) {
                return {
                    q: params.term,
                    page: params.page || 0,
                    pageSize: 10
                };
            },
            processResults: function (data) {
                
                currentPhieuGiamGias=data.content
                return {
                    results: data.content,
                    pagination: {
                        more: !data.last
                    }
                };
            }

        },
        maximumSelectionLength: 1


    });
    $('#hd-voucher').val([]).trigger('change');



}
function updateBillStep2(soTienThanhToan,soTienGiam){
    totalPrice=soTienThanhToan;
    maGiamGiaGiam=soTienGiam?soTienGiam:0;
    const stk="XUNGCONGQUY";
    const tenTaiKhoan="NGUYEN%20BA%20CHUC";
    const noiDung="ABCXYZ";
    let imgSrc;
    if(soTienGiam){
       imgSrc=`https://img.vietqr.io/image/tpbank-${stk}-compact2.jpg?amount=${soTienThanhToan-soTienGiam}&addInfo=${noiDung}&accountName=${tenTaiKhoan}`;
        $('#httt-tien-mat > span > span:nth-child(1)').text("Tổng hóa đơn: "+toCurrency(soTienThanhToan))
        $('#httt-tien-mat > span > span:nth-child(2)').text("Phiếu giảm giá: -"+toCurrency(soTienGiam))
        $('#httt-tien-mat > span > span:nth-child(3)').text("Thanh toán: "+toCurrency(soTienThanhToan-soTienGiam));

    }else{
        let imgSrc=`https://img.vietqr.io/image/tpbank-${stk}-compact2.jpg?amount=${soTienThanhToan}&addInfo=${noiDung}&accountName=${tenTaiKhoan}`
        $('#httt-tien-mat > span > span:nth-child(1)').text("")
        $('#httt-tien-mat > span > span:nth-child(2)').text("")
        $('#httt-tien-mat > span > span:nth-child(3)').text("Thanh toán: "+toCurrency(soTienThanhToan));

    }
    $('.qrcode').attr('src',imgSrc)
}

function checkDuplicate(form,selector){
    let values = [];
    let duplicates = [];
    form.find(selector).each(function() {
        let value = $(this).val().trim();
        if (value && values.includes(value)) {
            duplicates.push($(this));
        } else {
            values.push(value);
        }
    });
    if(duplicates.length > 0){
        duplicates.forEach(duplicateElm=>{duplicateElm.addClass('is-invalid')})
        return false;
    }
    return true;
}

$(document).ready(()=>{

    $('#form-check-out').off();
    $('#form-check-out').on('submit', function (event) {
        event.preventDefault();
        let form=$(this);
        if (!form[0].checkValidity()) {
            form.find(":invalid").removeClass("is-valid").addClass("is-invalid");
            form.find(":valid").prop("valid",false)
            event.stopPropagation();
        }
        if(form.find('.is-invalid').length === 0){
            $('#offcanvas-check-out').offcanvas('hide');
            $('#offcanvas-check-out-step-2').offcanvas('show');
            updateVoucherSelect()
        }
    });
    $('#offcanvas-check-out-step-2 > div.offcanvas-header > button').on('click', function () {
        $('#offcanvas-check-out').offcanvas('show');
        $('#offcanvas-check-out-step-2').offcanvas('hide');
        $('#offcanvas-check-out-step-2').find('form').removeClass('was-validated');
    })

    $('#nav-profile-tab').on('click',()=>{
        setTimeout(()=>{
            $('.qrcode')[0].scrollIntoView({ behavior: 'smooth', block: 'center' });},200);
    })

})


async function showConfirm(parentComponent,title,message){
    let $modal=$(`
        <div class="modal fade" id="ex-modal" tabindex="-1" aria-labelledby="ex-modalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="ex-modalLabel">${title}</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                ${message}
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary">Xác nhận</button>
              </div>
            </div>
          </div>
        </div>
    `);
}

$(document).ready(()=>{

    $('#form-check-out-step-2').on('submit', function (event) {
        event.preventDefault();
        let form=$(this);
        if (!form[0].checkValidity()) {
            event.stopPropagation();
        }
        form.addClass('was-validated')
        if(form.find('.is-invalid').length === 0){
            //Tạo đối tượng từ form
            let formData = {
                "khachHangId": $('#hd-khach-hang').val()?$('#hd-khach-hang').val()[0]:null,
                "phieuGiamGiaId": $('#hd-voucher').val()?$('#hd-voucher').val()[0]:null,
                "phuongThucThanhToan": $('#hinhThucTT .active').index()?"TIEN_MAT":"CHUYEN_KHOAN",
                "tongTien": totalPrice,
                "giamMaGiamGia": maGiamGiaGiam,
                "ngayNhanHang": new Date(),
                "ghiChu": $('#hd-ghi-chu').val(),
                "loaiHoaDon": "TAI_QUAY",
                "trangThai": "THANH_CONG",
                "hoaDonChiTiets": Array.from(_mapInvoice.get(_selectedInvoice) || [], ([key,val]) => ({
                    sanPhamChiTietId: val.spct.id,
                    dotGiamGiaId: val.spct.dotGiamGia?.id ?? null,
                    giaGoc: val.spct.giaBan,
                    giaKhuyenMai: val.spct.giaChietKhau,
                    soLuong: val.soLuong
                }))
            }
            console.log(formData)
            // Nếu form hợp lệ, gửi dữ liệu form lên server
            $.ajax({
                url: '/admin/api/v1/sale', // Thay 'URL_API' bằng đường dẫn của API của bạn
                method: 'PUT', // Phương thức HTTP
                data: JSON.stringify(formData),
                contentType: 'application/json',
                success: function (response) {
                    showSuccessToast('Thanh toán thành công')

                    $('#modal-add').modal('hide');
                    reloadDataTable();
                    console.log(response);
                },
                error: function (xhr, status, error) {
                    showErrToast('Lỗi không xác định',xhr.responseJSON.message)
                    reloadDataTable();
                    console.log(response);
                    console.error(xhr.responseText);
                }
            });



        }
    });


})