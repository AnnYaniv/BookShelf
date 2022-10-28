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
