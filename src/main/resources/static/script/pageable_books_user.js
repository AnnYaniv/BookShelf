function getPage(page) {
    $.ajax({
        type: "GET",
        url: "/book/get-by-user",
        data: {page: page},
        success: function (data, textStatus, jQxhr) {
            console.log("pageable products " + 1);
            $('#data-container').html(data +
            "<script src=\"script/pageable_books_user.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}