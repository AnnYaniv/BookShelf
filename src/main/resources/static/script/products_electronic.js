function onPageFilter(page) {
    $.ajax({
        type: "GET",
        url: "/book/get-by-user-electronic",
        data: {page: page},
        success: function (data, textStatus, jQxhr) {
            console.log("pageable products " + 1);
            $('#data-container').html(data +
                "<script src=\"script/products_electronic.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}