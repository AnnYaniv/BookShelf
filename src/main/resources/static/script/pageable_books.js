function getPage(page) {
    $.ajax({
        type: "GET",
        url: "/pageable",
        data: {page: page},
        success: function (data) {
            console.log("get by name " + page);
            $('#data-container').html(data +
                "<script src=\"script/pageable_books.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}