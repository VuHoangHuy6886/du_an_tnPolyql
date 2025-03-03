const hideModal = () => {
    $('.modal').each(function () {
        $(this).modal('hide');
    });
}
const showSuccessToast = (message) => {
    let Toast = $(`
        <div class="toast" style="position: fixed; bottom: 5%; right: 0; z-index: 9999999;width: 25%" data-delay="5100"  role="alert" aria-live="assertive" aria-atomic="true">
        <div class="alert alert-success mb-1" role="alert">
        <span>${message}</span>
    </div>
</div>
    `)
    $('body').append(Toast);
    Toast.toast('show');
}
const showErrorToast = (message) => {
    let Toast = $(`
        <div class="toast" style="position: fixed; bottom: 5%; right: 0; z-index: 9999999;width: 25%" data-delay="5100"  role="alert" aria-live="assertive" aria-atomic="true">
        <div class="alert alert-danger mb-1" role="alert">
        <span>${message}</span>
    </div>
</div>
    `)
    $('body').append(Toast);
    Toast.toast('show');
}
const showWarnigToast = (message) => {
    let Toast = $(`
        <div class="toast" style="position: fixed; bottom: 5%; right: 0; z-index: 9999999;width: 25%" data-delay="5100"  role="alert" aria-live="assertive" aria-atomic="true">
        <div class="alert alert-warning mb-1" role="alert">
        <span>${message}</span>
    </div>
</div>
    `)
    $('body').append(Toast);
    Toast.toast('show');
}

const clearForm = (selector) => {
    let formNeedClear=selector?selector:(document);
    console.log('Clear form')
    // Clear input fields
    $(formNeedClear).find('input[type="text"], input[type="password"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="date"], input[type="datetime-local"], input[type="month"], input[type="week"], input[type="time"]').val('');

    // Uncheck all checkboxes and radio buttons
    $(formNeedClear).find('input[type="checkbox"], input[type="radio"]').prop('checked', false);

    // Clear textarea fields
    $(formNeedClear).find('textarea').val('');

    // Reset select fields to default option
    $(formNeedClear).find('select').prop('selectedIndex', 0);


    $(formNeedClear).find('.was-validated').removeClass('was-validated');


    $(formNeedClear).find('.is-valid').removeClass('is-valid');


    $(formNeedClear).find('.is-invalid').removeClass('is-invalid');


}

function showSpinner($rootContainer){
    $rootContainer.css({"position":"relative"});
    $rootContainer.append(`
        <div class="text-center text-light ultil-spinner-overlay" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.5); display: flex; justify-content: center; align-items: center; z-index: 9999;">
          <div class="spinner-border" role="status">
            <span class="sr-only">Loading...</span>
          </div>
        </div>
    `)
}
function hideSpinner($rootContainer){
    $rootContainer.find('.ultil-spinner-overlay').remove();
}

function dataToJson(event) {
    return new Promise((resolve, reject) => {
        const file = event.target.files[0];
        const reader = new FileReader();

        reader.onload = function (e) {
            try {
                const data = new Uint8Array(e.target.result);
                const workbook = XLSX.read(data, {type: 'array'});

                // Giả sử dữ liệu nằm trong sheet đầu tiên
                const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
                const jsonData = XLSX.utils.sheet_to_json(firstSheet);

                resolve(jsonData)
                console.log(jsonData)
                event.target.files[0].value = ""
            } catch (e) {
                reject(e)
            }
        };


        reader.readAsArrayBuffer(file);
    })
}

const showConfirm = (title, message) => {
    console.log("showConfirm");

    return new Promise((resolve, reject) => {
        $('#modal-confirm-title').html(title);
        $('#modal-confirm-message').html(message);
        $('#modal-confirm').modal('show');

        // Hủy bỏ các sự kiện click trước đó để tránh đăng ký nhiều lần
        $('#modal-confirm-confirm').off('click').on('click', () => {
            console.log("OK");
            $('#modal-confirm').modal('hide');
            resolve(true);
        });

        $('#modal-confirm-cancel').off('click').on('click', () => {
            console.log("Cancel");
            $('#modal-confirm').modal('hide');
            reject(false);
        });
    });
}

$(document).ready(()=>{


    let toastContainer=$(".toast-container");
    if(toastContainer.length===0){
        $("body").append(`
            <div class="toast-container position-fixed bottom-0 left-0 p-3" style="z-index: 99999; bottom: 0;">
            <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
                <div class="toast-header">
                    <img src="..." class="rounded me-2" alt="...">
                    <strong class="me-auto">Bootstrap</strong>
                    <small>11 mins ago</small>
                    <button type="button" class="ms-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="toast-body">
                    Hello, world! This is a toast message.
                </div>
            </div>
        </div>
    
    `)
    }else{
        toastContainer.html(`
           
            <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">
                <div class="toast-header">
                    <img src="..." class="rounded me-2" alt="...">
                    <strong class="me-auto">Bootstrap</strong>
                    <small>11 mins ago</small>
                    <button type="button" class="ms-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="toast-body">
                    Hello, world! This is a toast message.
                </div>
            </div>
        
    
    `)
    }



})

function showSsToast(title, message) {
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

function showErToast(title, message) {
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
function showWnToast(title, message) {
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
function printDiv($document,$div,title){
    var divContents = $div.html();
    var printWindow = window.open('', '', `height=${$div.height()},width=1200`);
    var head=$document.find('head').html();
    printWindow.document.write('<html>'+head);
    printWindow.document.write('<body>');
    printWindow.document.write(`<div><h1>${title}</h1></div>`);
    printWindow.document.write(divContents);
    printWindow.document.write('</body></html>');
    printWindow.document.close();
    printWindow.print();
}