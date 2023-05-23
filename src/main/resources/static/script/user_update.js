$(function () {
    $('#update').on('click', function (e) {
        console.log("post update user");

        var visitorDto = {
            email: $('#email').val(),
            password: "",
            userName: $('#username').val()
        };

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
        e.preventDefault();
    })
});

function purchase(subscription){
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

function unsubscribe(){
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