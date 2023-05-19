function getPage(page) {
    const title = $('#search-input').val();
    $.ajax({
        type: "GET",
        url: "/book/get-by-name",
        data: {name: title, page: page},
        success: function (data) {
            console.log("get by name " + page);
            $('#data-container').html(data +
                "<script src=\"script/pageable_books_name.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}