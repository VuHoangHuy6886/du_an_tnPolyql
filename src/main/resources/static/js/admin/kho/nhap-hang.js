$(document).ready(function () {
    $('#select-san-pham').select2({
        placeholder: "Tìm kiếm theo mã hoặc theo tên",
        ajax: {
            url: '/api/v1/select2/san-pham',
            delay: 500,
            data: function (params) {
                return {
                    searchKey: params.term,
                    page: params.page || 0,
                    pageSize: 10
                };
            },
            cache: true

        },
        closeOnSelect: false


    });

})

$(document).ready(function () {
    $('#btn-add').on('click', function () {
        $('#select-san-pham').select2('data').forEach(data => {
            addToTable(data.id, data.text, 1)
        })
        $('#select-san-pham').val([]).trigger('change')
    })
})
function deleteSPCT(spctID){
    $(`#table-san-pham tr[spct-id="${spctID+""}"]`).removeAttr("onclick")
    $(`#table-san-pham tr[spct-id="${spctID+""}"]`).remove();
    $(`#table-imei tbody>tr[spct-id="${spctID+""}"]`).remove();


}
function addToTable(id, name, quantity) {
    if(quantity===0) return;
    function reindex() {
        $('#table-san-pham tbody tr ').find('td:nth-child(1)').each(
            function (index, elem) {
                $(elem).text(index + 1)
            })
    };
    let index = 1
    $('#table-san-pham tbody tr').find('td:nth-child(2)').each(function (index, elem) {
        if (elem.innerText == id) {
            showErToast("Lỗi", "Sản phẩm đã tồn tại")
        }

    })
    $('#table-san-pham tbody').append(`
        <tr spct-id="${id}" onclick="showImei($(this))">
            <td>${index}</td>
            <td class="id-spct">${id}</td>
            <td>${name}</td>
            <td class="sl-spct" contenteditable="true">${quantity}</td>
            <td><button class="btn btn-sm btn-danger" onclick="deleteSPCT(${id})">Xóa</button></td>

        </tr>
    `)
    reindex();
    showImei()
}


function showImei($row) {
    if (!$row) {
        $row = $('#table-san-pham tbody tr:last-child');
    }
    $row.closest('tbody').find('.active').removeClass('active')
    $row.addClass('active')
    let idSPCT = $row.find('.id-spct').text();
    let soLuong = 1;
    if (Number($row.find('.sl-spct').text())) {
        soLuong = parseInt($row.find('.sl-spct').text());
    } else {
        showErToast("Lỗi", "Kiểm tra số lượng")
        return;
    }

    $(`#table-imei tbody tr`).hide();
    let $rows = $(`#table-imei tbody tr[spct-id="${idSPCT}"]`)
    if ($rows.length < soLuong) {
        let dataRow = ''
        for (let i = 0; i < (soLuong - $rows.length); i++) {
            dataRow += `
                <tr spct-id="${idSPCT}">
                   <td style="vertical-align: middle; text-align: center">${i + 1}</td>
                   <td> 
                       <div class="input-group has-validation">
       
                        <input pattern="\\w{15}" required class="form-control" type="text" name="${idSPCT}" onblur="sync($(this))">
                          <div class="invalid-feedback">
                            IMEI Không hợp lệ
                          </div>
                        </div>
                    </td>
                </tr>
            `
        }

        $("#table-imei tbody").append(dataRow);
    }
    $(`#table-imei tbody tr[spct-id="${idSPCT}"]`).each(function (index, elem) {

        if (index < soLuong) {
            $(elem).show()
        }else{
            $(elem).remove();
        }
    })
    validSPCT();
    reIndexImei();


}
let debouce;
function reIndexImei(){
    $("#table-imei tbody tr").filter(function (){
        return $(this).css("display")!=="none"
    }).each(function (index,elm) {
        $(elm).find("td:first").html(index+1);
    })
}


function validSPCT() {
    $('#table-san-pham tr.text-danger').removeClass("text-danger");
    $('#table-imei tbody .is-invalid').each((index, val) => {
        let spctId = $(val).closest("tr").attr('spct-id');
        $(`#table-san-pham tr[spct-id="${spctId}"]`).addClass("text-danger");
    })
}

async function sync($imeiRow) {
    let imei = $imeiRow.val();
    if (imei.length === 0) {
        return;
    } else if (imei.length != 15) {
        $imeiRow.addClass("is-invalid");
        showErToast("Lỗi", "Độ dài  15 ký tự");
        return;
    }
    let check = 0;
    $('#table-imei input').each((index, val) => {
        if ($(val).val() === imei) {
            check++;
        }
    })
    if (check !== 1) {
        $imeiRow.addClass("is-invalid");
        showErToast("Lỗi", "IMEI là duy nhất");
        return;
    }
    let data = await fetch(`/api/v1/imei/${imei}`)
    switch (data.status) {
        case 302: {
            $imeiRow.addClass("is-invalid");
            showErToast("Lỗi", "Imei đã tồn tại");
            break;
        }
        case 404: {
            $imeiRow.removeClass("is-invalid").addClass("is-valid");
            break;
        }
        default: {
            $imeiRow.addClass("is-invalid");
            showErToast("Lỗi", "Lỗi không xác định");
            break;
        }
    }
}

$(document).ready(() => {
    const $tableSPCT = $('#table-san-pham'),
        $tableIMEI = $('#table-imei')
    $('#import-file').on("change", (event) => {
        showSpinner($('#content > div > div.row > div'))
        dataToJson(event)
            .then(jsonData => {
                let dataName = jsonData.map(item => item['Mã sản phẩm chi tiết']);
                let dataImei = jsonData.reduce((acc,item)=> [...acc,...(item['IMEI(Phân tách bởi dấu \",\")'].split(','))],[])
                let mapSPCT=new Map();
                let mapImei=new Map();
                let promiseSPCT= $.ajax({
                    url:  "/api/v1/kho/get-name",
                    type: 'POST',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(dataName),
                })
                let promiseIMEI=$.ajax({
                    url:"/api/v1/kho/check-imei",
                    type: 'POST',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(dataImei),
                })
                Promise.all([promiseSPCT,promiseIMEI]).then(([spctResults,imeiResults])=>{
                    mapSPCT=spctResults.reduce((acc,item)=> acc.set(item[0],item[1]),new Map())
                    mapImei=imeiResults.reduce((acc,item)=> acc.set(item[0],item[1]),new Map())
                    jsonData.forEach(item=>{
                        let spctId = item['Mã sản phẩm chi tiết'];
                        let imeis = item['IMEI(Phân tách bởi dấu \",\")'].split(',');
                        imeis.forEach(imei=>{
                            let $row=$(`
                                <tr spct-id="${spctId}">
                                       <td style="vertical-align: middle; text-align: center"></td>
                                       <td> 
                                           <div class="input-group has-validation">
            
                                            <input pattern="\\w{15}" required class="form-control ${mapImei.get(imei)?"is-valid":"is-invalid"}" name="${spctId}" type="text" value="${imei}" onblur="sync($(this))">
                                              <div class="invalid-feedback">
                                                IMEI Không hợp lệ
                                              </div>
                                            </div>
                                        </td>
                                    </tr>
                            `)
                            $tableIMEI.find('tbody').append($row);
                            $row.hide();
                        })

                        addToTable(spctId,mapSPCT.get(spctId),imeis.length);
                        hideSpinner($('#content > div > div.row > div'))
                    })

                })
            })
            .catch(e =>{
                showErToast("Lỗi: " + e);
                hideSpinner($('#content > div > div.row > div'))

                }


            )
    })
})
function nhapHang(){

    // validate

    let formData=new Map();
    let flag=true;
    if($('#table-imei input').length == 0){
        showErToast("Lỗi", "Vui lòng chọn thêm mặt hàng");
        return;
    }
    $('#table-imei input').each((i,item)=>{
        let check=item.checkValidity();

        let $item=$(item)
        if(!check){
            flag=false;
            $item.addClass("is-invalid");
        }else{
            $item.removeClass("is-invalid");
        }
        let set=formData.get($item.attr("name"))??new Set();
        set.add($item.val());
        formData.set($item.attr("name"),set);
    })
    if(!flag){
        showErToast("Lỗi", "Vui lòng kiểm tra các trường");
        validSPCT();
        return;
    }
    let ghiChu=$('#note').val();
    let dataPost={
        data:[...formData].reduce((acc,[key,val])=> {
            acc.push({id:key,imeis:Array.from(val)});
            return acc;
        },[]),
        note:ghiChu
    }


    $.ajax({
        url: '/api/v1/kho/nhap-hang',
        type: 'POST',
        data: JSON.stringify(dataPost),
        contentType: 'application/json',
        success: function(response) {
            // Xử lý khi yêu cầu thành công
            showSsToast("Thành công","Đã nhập hàng thành công");
            setTimeout(()=>{window.location.replace('/admin/kho')},500);

        },
        error: function(jqXHR, textStatus, errorThrown) {
            // Xử lý khi có lỗi
            showErToast("Thất bại","Vui lòng kiểm tra các trường thông tin");
            console.error(errorThrown);
        }
    });
}
