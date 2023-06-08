function getPage(page) {
    $.ajax({
        type: "GET",
        url: "/pageable",
        data: {page: page},
        success: function (data) {
            console.log("get all " + page);
            $('#selection_books').html(data +
                "<script src=\"script/pageable_books.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}