function onPageFilter(page) {
    $.ajax({
        type: "GET",
        url: "/favourite/pageable",
        data: {page: page},
        success: function (data, textStatus, jQxhr) {
            console.log("pageable products " + 1);
            $('#selection_books').html(data);
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}