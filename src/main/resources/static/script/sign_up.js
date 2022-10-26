(function () {
    'use strict'
    var forms = document.querySelectorAll('.needs-validation')
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                } else {
                    $.ajax({
                        type: "POST",
                        url: "/auth",
                        data: {
                            email: $('#email').val(),
                            password: $('#password').val(),
                            userName: $('#userName').val()
                        },
                        success: function(data){
                            console.log(data);
                        }
                    });
                    event.preventDefault()
                    event.stopPropagation()
                }
                form.classList.add('was-validated')
            }, false)
        })
})()