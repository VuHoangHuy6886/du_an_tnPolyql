

const container = $('#container');
const registerBtn = $('#register');
const loginBtn = $('#login');

let url=window.location.href;
if(url.includes("sign-up")){
    container.addClass("active");
    $('title').html("Đăng ký")
}

registerBtn.on('click', () => {
    console.log(container)
    container.addClass("active");
    $('title').html("Đăng ký")
});

loginBtn.on('click', () => {
    container.removeClass("active");
    $('title').html("Đăng nhập")
});

$(document).ready(function () {
    $('#loginForm').on('submit', function (event) {
        event.preventDefault();

        var isValid = true;
        $('#loginForm input').each(function () {
            if (!this.checkValidity()) {
                isValid = false;
                $(this).addClass('is-invalid');
            } else {
                $(this).removeClass('is-invalid');
            }
        });

        if (isValid) {
            var username = $('#email').val();
            var password = $('#password').val();

            $.ajax({
                type: 'POST',
                url: '/sign-in',
                data: JSON.stringify( {
                    email: username,
                    password: password
                }),
                contentType: 'application/json',

                success: function (response) {
                    // Lưu token vào localStorage nếu cần
                    if (response.token) {
                        localStorage.setItem('Authorization',"Bearer "+ response.token);
                        Toast.fire({
                            icon:"success",
                            title:"Đăng nhập thành công"
                        })
                    }
                    // Chuyển hướng người dùng đến trang chính hoặc trang trước đó (nếu có)
                    let successUrl=new URLSearchParams(window.location.href).get('successUrl')
                    if(successUrl)
                        window.location.href = successUrl;
                    else
                        window.location.href = "/home";
                },
                error: function (response) {
                   showErToast("Lỗi","Tên đăng nhập hoặc mật khẩu không chính xác")
                }
            });
        }
    });
    $('#registForm').on('submit', function (event) {
        event.preventDefault();

        let isValid =this.checkValidity();
        $(this).find('input').each((i,e)=>{
            if (!e.checkValidity()) {
                e.classList.add('is-invalid');
                isValid =false;
            }else{
                e.classList.add('is-valid');
                e.classList.remove('is-invalid');

            }
        })
        if (isValid) {
            let formData={
                ten: $(this).find("input[name='ten']").val(),
                email: $(this).find("input[name='email']").val(),
                soDienThoai: $(this).find("input[name='soDienThoai']").val(),
                matKhau: $(this).find("input[name='matKhau']").val()
            }

            $.ajax({
                type: 'POST',
                url: '/sign-up',
                data: JSON.stringify(formData),
                contentType: 'application/json',

                success: function (response) {
                    window.location.href="/verify-account";
                },
                error: function (xhr) {
                    xhr.responseJSON.forEach(err=>{
                        let fieldName=err.field;
                        let messageErr=err.defaultMessage;
                        $(`#registForm input[name='${fieldName}']`).addClass('is-invalid')
                        showErToast("Lỗi",messageErr)
                    })

                }
            });
        }
    });

});