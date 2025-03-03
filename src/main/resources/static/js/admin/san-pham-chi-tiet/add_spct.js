let initFlagModalAdd=true;
window.addEventListener("DOMContentLoaded",function () {
    if(!initFlagModalAdd)
        return;
    initFlagModalAdd=false;

    $('.image-input').on('change, input',function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.addEventListener('load', function() {
                $('.image-preview').attr('src',this.result) // gán URL của ảnh cho src của img
                $('.image-preview').removeClass('d-none') // gán URL của ảnh cho src của img
                // hiển thị ảnh preview
            });
            reader.readAsDataURL(file); // chuyển file thành Data URL
        } else {
            $('.image-preview').addClass('d-none')
        }
    })
    $('#modal-spct form').on('submit', function (event) {
        if($(this).find("input[type='file']").length>0){
            const $form = $(this);
            event.preventDefault();
            if (!$form[0].checkValidity()) {
                event.stopPropagation();
                $form.addClass('was-validated');
            } else {
                $.ajax({
                    url: $form.attr('action'),
                    method: 'PUT',
                    data: new FormData($form[0]),
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        Toast.fire({
                            icon: "success",
                            title: "Thêm mới thành công"
                        })
                        $form.closest('.modal').modal('hide');
                        updateNewOption($form,response)
                    },
                    error: function (xhr, status, error) {
                        Toast.fire({
                            icon: "error",
                            title: xhr.responseJSON.error
                        });
                    }
                });

            }
        }else{
            let $form = $(this);
            event.preventDefault();
            if (!$form[0].checkValidity()) {
                event.stopPropagation();
                $form.addClass('was-validated');
            }

            let formData = {};
            $form.serializeArray().forEach(function (item) {
                formData[item.name] = item.value;
            });
            $.ajax({
                url: $form.attr('action'),
                type: 'PUT',
                data: JSON.stringify(formData),

                contentType: 'application/json',
                success: function (response) {
                    Toast.fire({
                        icon: "success",
                        title: "Thêm mới thành công"
                    })
                    $form.closest('.modal').modal('hide');
                    updateNewOption($form,response)


                },
                error: function (xhr, status, error) {
                    Toast.fire({
                        icon: "error",
                        title: xhr.responseJSON.error
                    });
                    console.log(response);
                    console.error(xhr.responseText);
                }
            })
        }



    })
    function updateNewOption($form,data){
        debugger
        let newOption = new Option(data.ten, data.id, true, true);
        let $select=$(`a[data-target="#${$form.closest('.modal').attr('id')}"]`).closest('.form-group').find('select')
        $select.append(newOption).trigger("change");
    }
})