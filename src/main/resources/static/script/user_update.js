$(function () {
    $('#update').on('click', function (e) {
        console.log("post update user");
        const repeat = document.getElementById('repeat');
        const password = document.getElementById('password');
        const email = document.getElementById('email');
        const username = document.getElementById('username');

        [repeat, password, email, username].forEach(element => {
            element.classList.remove('is-invalid')
            element.classList.remove('is-valid')
        });

        var visitorDto = {
            email: $('#email').val(),
            password: "",
            userName: $('#username').val()
        };
        let isCorrect = true;
        if (!(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email.value))) {
            email.classList.add('is-invalid');
            isCorrect = false;
        } else {
            email.classList.add('is-valid')
        }

        if (username.value.length < 8) {
            username.classList.add('is-invalid');
            isCorrect = false;
        } else {
            username.classList.add('is-valid')
        }

        if (password.value.length !== 0) {
            if (password.value.length < 8) {
                password.classList.add('is-invalid');
                isCorrect = false;
                repeat.value = "";
            } else {
                password.classList.add('is-valid');
                if (repeat.value !== password.value) {
                    repeat.classList.add('is-invalid');
                    visitorDto.password = password.value;
                    isCorrect = false;
                } else {
                    repeat.classList.add('is-valid')
                }
            }
        }
        if (isCorrect) {
            console.log(isCorrect);
            $.ajax({
                type: "POST",
                url: "/user/about",
                data: visitorDto,
                success: function (data, textStatus, jQxhr) {
                    $('#data-container').html(data);
                },
                error: function (jqXhr, textStatus, errorThrown) {
                    console.log(errorThrown);
                }
            });
        }
    })
});

function purchase(subscription) {
    $.ajax({
        type: "POST",
        url: "/user/subscribe",
        data: {
            time: subscription
        },
        success: function (data, textStatus, jQxhr) {
            location.reload();
            console.log("expires at " + data);
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function unsubscribe() {
    $.ajax({
        type: "POST",
        url: "/user/unsubscribe",
        success: function (data, textStatus, jQxhr) {
            location.reload();
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}