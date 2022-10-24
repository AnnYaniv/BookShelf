function onPageFilter(page) {
    if($('#filter_input').text().length > 1) {
        var filterDto = JSON.parse($('#filter_input').text());
        filterDto.page = page;
        console.log(filterDto);

        $.ajax({
            type: "GET",
            url: "/book/getFilterBy",
            data: filterDto,
            success: function (data, textStatus, jQxhr) {
                console.log("page by filter")
                $('#name_input').text("");
                $('#selection_books').html(data);
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
    } else {
        if($('#name_input').text.length > 0){
            $.ajax({
                type: 'GET',
                url: '/book/filter?name=' + $('#name_input').text() +"&page="+ page,
                success: function (result) {
                    console.log("page by name")
                    $('#selection_books').html(result);
                }
            });
        }
        else {
            $.ajax({
                type: "GET",
                url: "/pageable?page=" + page,
                success: function (data, textStatus, jQxhr) {
                    console.log("without anything");
                    $('#selection_books').html(data);
                },
                error: function (jqXhr, textStatus, errorThrown) {
                    console.log(errorThrown);
                }
            });
        }
    }
}

$('#search-input').change(function () {
    $.ajax({
        type: 'GET',
        url: '/book/filter?name=' + $('#search-input').val()+"&page=0",
        success: function (result) {
            $('#name_input').text($('#search-input').val());
            $('#selection_books').html(result);
        }
    });
});

$(function () {
    $('#ajax-tester-btn').on('click', function (e) {
        var genre = [];
        document.querySelectorAll('input[name="genre"]:checked').forEach((element) =>
            genre.push(element.id)
        )
        var filterDto = {
            min: $('#min_input').val(),
            max: $('#max_input').val(),
            sort: document.querySelector('input[name="sort"]:checked').value,
            genre: genre,
            page: 0
        };

        $.ajax({
            type: "GET",
            url: "/book/getFilterBy",
            data: filterDto,
            success: function (data, textStatus, jQxhr) {
                console.log("we get a response");
                $('#filter_input').text(JSON.stringify(filterDto));
                $('#name_input').text("");
                console.log($('#filter_input').text());
                $('#selection_books').html(data);
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }

        });
        e.preventDefault();
    })
});