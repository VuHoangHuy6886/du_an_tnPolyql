var id = window.location.pathname.split('/').pop()
var sanPham = {
    "id": 19,
    "tenSanPham": "iPhone 14 Pro Max",
    "binhLuan": null,
    "sanPhamChiTiet": [
        {
            "id": 1188,
            "khuyenMai": [],
            "anh": [],
            "mauSac": {
                "ten": "Titan đen",
                "ma": "#1C1C1E"
            },
            "rom": "256GB",
            "giaBan": 20682446,
            "trangThai": "IN_STOCK"
        }
    ],
    "thoiGianBaoHanh": "24 Tháng",
    "trangThai": "IN_STOCK",
    "moTa": "<p style=\"text-align: center;\">&nbsp;<img class=\" lazyloaded\" title=\"iPhone 15 Pro Max 256GB Tổng Quan\" src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-210923-113413.jpg\" alt=\"iPhone 15 Pro Max 256GB Tổng Quan\" width=\"1000\" height=\"592\" data-src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-210923-113413.jpg\"><img class=\" lazyloaded\" title=\"iPhone 15 Pro Max 256GB Th&ocirc;ng Số Kỹ Thuật V&agrave; T&iacute;nh Năng\" src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-210923-113413-1.jpg\" alt=\"iPhone 15 Pro Max 256GB Th&ocirc;ng Số Kỹ Thuật V&agrave; T&iacute;nh Năng\" width=\"1000\" height=\"1952\" data-src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-210923-113413-1.jpg\"><img class=\" lazyloaded\" title=\"iPhone 15 Pro Max 256GB So S&aacute;nh\" src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-051023-102933.jpg\" alt=\"iPhone 15 Pro Max 256GB So S&aacute;nh\" width=\"1000\" data-src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-051023-102933.jpg\"><img class=\" lazyloaded\" title=\"iPhone 15 Pro Max 256GB Chuyển Đổi\" src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-210923-113414-1.jpg\" alt=\"iPhone 15 Pro Max 256GB Chuyển Đổi\" width=\"1000\" data-src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-210923-113414-1.jpg\"><img class=\" lazyloaded\" title=\"iPhone 15 Pro Max 256GB Phụ Kiện\" src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-271123-053904.jpg\" alt=\"iPhone 15 Pro Max 256GB Phụ Kiện\" width=\"1000\" data-src=\"https://cdn.tgdd.vn/Products/Images/42/305658/s16/iphone-15-pro-max-271123-053904.jpg\">&nbsp;</p>"
}
var selectedId = -1;
const cloneData = function (url) {
    return new Promise(function (resolve, reject) {
        $.ajax({
                url: url,
                success: function (response) {
                    sanPham = response;
                    resolve(response);
                },
                error: function (xhr, status, error) {
                    reject(status);
                }
            }
        )
    })
}
const documentReadyPromise = new Promise((resolve, reject) => {
    $(document).ready(() => {
        resolve();
    });
});
function toCurrency(Num){
    Num = Num<=0?0:Num;
    return (numeral(Num).format('0,0') + ' VND').replace(/,/g,".")
}

function textDD_MM_YYYYtoDate(stringDate){
    return new Date(stringDate.split('-').reverse().join('-'));
}

Promise.all([cloneData('/api/v2/san-pham/' + id), documentReadyPromise]).then(() => {
    let uniqueProductRoms = new Map();
    sanPham.sanPhamChiTiet.forEach(spct => {
        if (uniqueProductRoms.get(spct.rom) == undefined) {
            uniqueProductRoms.set(spct.rom, [spct]);
        } else {
            uniqueProductRoms.set(spct.rom, [...uniqueProductRoms.get(spct.rom), spct]);
        }

    })
    uniqueProductRoms = new Map([...uniqueProductRoms.entries()].sort((a, b) => a[0].replace(/1TB/g, '6').localeCompare(b[0].replace(/1TB/g, '6'))))
    uniqueProductRoms.forEach((value, key) => {
        value.sort((a, b) => a.giaBan > b.giaBan ? -1 : 0);
        let mapVal = new Map(value.map(i => [i.mauSac.ma, i]));
        uniqueProductRoms.set(key, mapVal);
    })

    let romBtnHtml = ''
    let firstRomFlag = ''
    uniqueProductRoms.forEach((value, key) => {
        romBtnHtml += `
                            <label class="btn btn-secondary">
                                <input type="radio" name="selected-rom" id='${key}'> ${key}
                            </label>
`
    })
    $('#container-btn-rom').on('click', 'input[name="selected-rom"]', function () {
        let mauSacBtnHtml = '';
        let rom = $(this).attr('id');
        uniqueProductRoms.get(rom).forEach((spct, maMauSac) => {
            mauSacBtnHtml += `
                <label class="btn btn-outline-secondary rounded-circle mr-2 mb-2" style="width: 30px;height: 30px; background-color: ${maMauSac}">
                    <input type="radio" name="selected-mau-sac"  rom-id="${rom}" id='${maMauSac}' spct-id="${spct.id}">
                </label>
`
        })
        $('#container-btn-mau-sac').find('.btn-group-toggle').html(mauSacBtnHtml);
        $('#container-btn-mau-sac').on('click', 'input[name="selected-mau-sac"]', function () {
            let rom = $(this).attr('rom-id');
            let mauSac = $(this).attr('id');
            let spct = uniqueProductRoms.get(rom).get(mauSac);
            $('#ten-mau-sac').html(spct.mauSac.ten);

            // khuyenMai
            {
                let giaSanPhamHtml = '';
                let giaBan=spct.giaBan;
                if(spct.dotGiamGia && !spct.dotGiamGia.deleted){
                    if(spct.dotGiamGia.donvi == '%')
                        giaSanPhamHtml = `
                                <div class="p-0 d-flex align-items-end">
                                <h5 class="mr-2 p-0 m-0 font-weight-bold text-light">Giá gốc:</h5><del class="h5 mt-2 price text-15 text-th">${toCurrency(spct.giaBan)}
                                    
                                </del>
                                <span class="ml-1 mb-2"> - ${ spct.dotGiamGia.giaTriGiam + '%'}</span>
                                
                                </div>
                                 <div class="p-0 d-flex align-items-end">
                                    <h5 class="mr-2 p-0 m-0 font-weight-bold text-light">Giá khuyến mại:</h5><div class="p-0"><span class="h5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan - spct.giaBan * spct.dotGiamGia.giaTriGiam / 100)}</span></div>
                                </div>
                                 `
                    else
                        giaSanPhamHtml = `
                                <div class="p-0 d-flex align-items-end">
                                <h5 class="mr-2 p-0 m-0 font-weight-bold text-light">Giá gốc:</h5><del class="h5 mt-2 price text-15 text-th">${toCurrency(spct.giaBan)}
                                    
                                </del>
                                <span class="ml-1 mb-2"> - ${toCurrency(spct.dotGiamGia.giaTriGiam)}</span>
                                
                                </div>
                                 <div class="p-0 d-flex align-items-end">
                                    <h5 class="mr-2 p-0 m-0 font-weight-bold text-light">Giá khuyến mại:</h5><div class="p-0"><span class="h5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan - spct.dotGiamGia.giaTriGiam)}</span></div>
                                </div>
                                 `
                }else{
                    giaSanPhamHtml = `
                            <div class="p-0 d-flex align-items-end">
                           
                             <div class="p-0 d-flex align-items-end">
                                <h5 class="mr-2 p-0 m-0 font-weight-bold text-light mb-1">Giá:</h5><div class="p-0"><span class="h5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                            </div>`
                }



                $('#sp-gia').html(giaSanPhamHtml)
            }

            //Hinh anh
            {
                let carouselIndicators=''
                let carouselInner=''
                for (let i = 0; i < spct.anh.length; i++) {
                    carouselIndicators+=`<li data-target="#carouselExampleIndicators" data-slide-to="${i}" class="${i==0?'active':''}"></li>`
                    carouselInner+=`<div class="carousel-item ${i==0?'active':''}">
                            <img src="${spct.anh[i].url}" class="d-block w-100" alt="${spct.anh[i].name}">
                        </div>`
                }
                $('.carousel-indicators').html(carouselIndicators)
                $('.carousel-inner').html(carouselInner)



            }



            selectedId = $(this).attr('spct-id');


            console.log('selected id' + selectedId);
        })
        $('#container-btn-mau-sac').find('input[name="selected-mau-sac"]').first().trigger('click');

    });
    $('#container-btn-rom').find('.btn-group-toggle').html(romBtnHtml)

    $('#container-btn-rom').find('input[name="selected-rom"]').first().trigger('click');

})
function initCarousel(){
    setTimeout(function(){
        $('.carousel-main').flickity('destroy');
        $('.carousel-nav').flickity('destroy');
        $('.carousel-main').removeClass('flickity-enabled')
        $('.carousel-main').removeClass('is-draggable')
        $('.carousel-nav').removeClass('flickity-enabled')
        $('.carousel-nav').removeClass('is-draggable')

        $('.carousel-main').flickity({
            pageDots: false
        });

        $('.carousel-nav').flickity({
            asNavFor: '.carousel-main',
            contain: true,
            pageDots: false
        });
    },500)
}
