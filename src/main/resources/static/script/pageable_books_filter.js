function getPage(page) {
    const genre = [];
    document.querySelectorAll('input[name="genre"]:checked').forEach((element) =>
        genre.push(element.id)
    )
    const filterDto = {
        min: $('#min_input').val(),
        max: $('#max_input').val(),
        sort: document.querySelector('input[name="sort"]:checked').value,
        genre: genre,
        page: page
    };
    $.ajax({
        type: "GET",
        url: "/book/get-by-filter",
        data: filterDto,
        success: function (data) {
            $('#filter_input').text(JSON.stringify(filterDto));
            $('#name_input').text("");
            $('#selection_books').html(data +
                "<script src=\"script/pageable_books_filter.js\"/>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}