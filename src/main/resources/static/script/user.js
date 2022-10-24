$(function () {
    $('#dashboard').on('click', function (e) {
        $.ajax({
            type: "GET",
            url: "/user/about",
            success: function (data, textStatus, jQxhr) {
                console.log("we get about");
                $('#data-container').html(data);
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }

        });
        e.preventDefault();
    })
});

$(function () {
    $('#invoices').on('click', function (e) {
        $.ajax({
            type: "GET",
            url: "/cart/all",
            success: function (data, textStatus, jQxhr) {
                console.log("we get about");
                $('#data-container').html(data);
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }

        });
        e.preventDefault();
    })
});