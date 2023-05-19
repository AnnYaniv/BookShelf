function getPage(page) {
    $.ajax({
        type: "GET",
        url: "/book/get-by-user-electronic",
        data: {page: page},
        success: function (data, textStatus, jQxhr) {
            console.log("electronic " + page);
            $('#data-container').html(data +
                "<script src=\"script/pageable_electronic_books.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}