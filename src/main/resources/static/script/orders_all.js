function onPageFilter(page) {
    $.ajax({
        type: "GET",
        url: "/cart/all",
        data: {page: page},
        success: function (data, textStatus, jQxhr) {
            console.log("pageable products " + 1);
            $('#data-container').html(data +
                "<script src=\"script/orders_all.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

