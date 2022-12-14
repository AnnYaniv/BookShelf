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
            data: {page: 0},
            success: function (data, textStatus, jQxhr) {
                console.log("we get about");
                $('#data-container').html(data+
                    "<script src=\"script/orders_all.js\"></script>");
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }

        });
        e.preventDefault();
    })
});

$(function () {
    $('#available').on('click', function (e) {
        $.ajax({
            type: "GET",
            url: "/book/get-by-user-electronic",
            data: {page: 0},
            success: function (data, textStatus, jQxhr) {
                console.log("pageable products " + 1);
                $('#data-container').html(data +
                    "<script src=\"script/products_electronic.js\"></script>");
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
        e.preventDefault();
    })
});

$(function () {
    $('#orders').on('click', function (e) {
        $.ajax({
            type: "GET",
            url: "/cart/manage",
            data: {page: 0, status: 'CREATING'},
            success: function (data, textStatus, jQxhr) {
                console.log("pageable products");
                $('#data-container').html(data +
                    "<script src=\"script/cart_manage.js\"></script>");
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
        e.preventDefault();
    })
});

$(function () {
    $('#products').on('click', function (e) {
        $.ajax({
            type: "GET",
            url: "/book/get-by-user",
            data: {page: 0},
            success: function (data, textStatus, jQxhr) {
                console.log("pageable products");
                $('#data-container').html(data +
                    "<script src=\"script/products_all.js\"></script>");
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
        e.preventDefault();
    })
});

