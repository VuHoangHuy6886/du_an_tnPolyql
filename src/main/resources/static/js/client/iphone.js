const cloneData=function (url) {
    return new Promise((resolve, reject) => {
        $.ajax({
                url: url,
                success: function (response) {
                    resolve(response);
                },
                error: function (xhr, status, error) {
                    reject(status);
                }
            }
        )
    })

}

$(document).ready(function(){
    let lstProduct=[];
    lstProduct=cloneData('/api/v2/san-pham').then(function(response){
        lstProduct=response;
        fillData(lstProduct)
    });
    lstProduct=cloneData('/api/v1/admin/data-list-add-san-pham/series').then(function(response){

        let rowSeries='<span class="selected" series="all">Tất cả</span>';
        response.results.forEach(rp=>{
            rowSeries+=`<span series-id="${rp.id}">${rp.text}</span>`
        })
        $('.fillter').html(rowSeries)
    });

})
const TrangThai=new Map([
    ['IN_STOCK',""],
    ['OUT_OF_STOCK',"Hết hàng"],
    ['TEMPORARILY_OUT_OF_STOCK',"Hết hàng tạm thời"],
    ['COMING_SOON',"Sắp ra mắt"],
    ['DISCONTINUED',"Không kinh doanh"]
])
function toCurrency(Num){
    Num = Num<=0?0:Num;
    return (numeral(Num).format('0,0') + ' VND').replace(/,/g,".")
}

function fillData(response){
    let productContainer="";
    let seriesMap=new Map();
    let mapProduct=new Map();

    response.forEach(sp=>{
        let uniqueProductRoms=new Map();
        sp.sanPhamChiTiet.forEach(spct=>{
            if(uniqueProductRoms.get(spct.rom)==undefined||uniqueProductRoms.get(spct.rom).giaBan>spct.giaBan){
                uniqueProductRoms.set(spct.rom,spct);
            }

        })
        uniqueProductRoms=new Map([...uniqueProductRoms.entries()].sort((a, b) => a[0].replace(/1TB/g,'6').localeCompare(b[0].replace(/1TB/g,'6'))))
        let romBtn="";
        let giaBan=99999999999;
        let giaSanPhamHtml='';
        let firstBtnFlag=true;
        uniqueProductRoms.forEach((spct,rom)=>{
            if(firstBtnFlag){
                romBtn+=`<button class="btn btn-outline-secondary btn-sm m-1 ${firstBtnFlag?'selected':''} ">${spct.rom}</button>`;
                if(spct.dotGiamGia){
                    giaSanPhamHtml=`
                    <div class="p-0 d-flex">
                    <del class="text-white h-5 mt-2 price text-15 text-th">${toCurrency(spct.giaBan)}
                        <span class="">-${spct.dotGiamGia.donvi=='%'?spct.dotGiamGia.giaTriGiam+'%':toCurrency(spct.dotGiamGia.giaTriGiam)}</span>
                    </del>
                    
                    </div>
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.dotGiamGia.donvi=='%'?spct.giaBan-spct.giaBan*spct.dotGiamGia.giaTriGiam/100:spct.giaBan-spct.dotGiamGia.giaTriGiam)}</span></div>
                `
                }else{
                    giaSanPhamHtml=`
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                    `
                }



            }else{
                romBtn+=`<button class="btn btn-outline-secondary btn-sm m-1 ${firstBtnFlag?'selected':''} ">${spct.rom}</button>`;

            }


            firstBtnFlag=false;
        })
        giaBan=toCurrency(giaBan)
        productContainer+=`
            <div class="product mb-5" sp-id="${sp.id}" style="width: 30%">
                <a class="" href="/iphone/${sp.id}">
                    <label>Bảo hành ${sp.thoiGianBaoHanh}</label>
                    <div><img src="${sp.anhUrl}" alt="${sp.anhName}" srcset=""></div>
                    <div class="p-0"><span >${TrangThai.get(sp.trangThai)}</span></div>
                    <div class="d-flex">${romBtn}</div>
                    
                    <div class="p-0"><span class="text-white">${sp.tenSanPham}</span></div>
<!--                 Giá sản phẩm   /-->
                    <div class="giaSanPham">${giaSanPhamHtml}</div>
                </a>
            </div>
        `
        function initPhieuGiamGia(dotGiamGia,giaBan) {

            return `
                <div className="p-0">
                    <del className="text-white h-5 mt-2 price text-15 text-th">${giaKhuyenMai}</del>
                </div>
            `
        }
        mapProduct.set(sp.id,uniqueProductRoms);

    })



    $('.product-container').first().html(productContainer);

    $('.product-container').on('click', 'button', function(event){
        event.stopPropagation()
        event.preventDefault()
        let product=$(this).closest('.product');
        let spId=parseInt($(this).closest('.product').attr('sp-id'));
        let uniqueProductRoms=mapProduct.get(spId);
        product.find('button').removeClass('selected');
        $(this).addClass('selected');
        let spct=uniqueProductRoms.get($(this).text());
        let giaSanPhamHtml='';
        if(spct.dotGiamGia){
            giaSanPhamHtml=`
                    <div class="p-0 d-flex">
                    <del class="text-white h-5 mt-2 price text-15 text-th position-relative">${toCurrency(spct.giaBan)}
                        <span class="" style="right: -45px;top: -10px">-${spct.dotGiamGia.donvi=='%'?spct.dotGiamGia.giaTriGiam+'%':toCurrency(spct.dotGiamGia.giaTriGiam)}</span>
                    </del>
                    
                    </div>
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.dotGiamGia.donvi=='%'?spct.giaBan-spct.giaBan*spct.dotGiamGia.giaTriGiam/100:spct.giaBan-spct.dotGiamGia.giaTriGiam)}</span></div>
                `
        }else{
            giaSanPhamHtml=`
                    <div class="p-0"><span class="text-white h-5 mt-2 price font-weight-bold">${toCurrency(spct.giaBan)}</span></div>
                    `
        }
        product.find('.giaSanPham').html(giaSanPhamHtml);

    })
}