function star_click(mark) {
    console.log("mark: " + mark);
    var star = "<i class='fa-solid fa-star fa-2x'></i>";
    var empty = "<i class='fa-regular fa-star fa-2x'></i>";
    $('#mark-input').val(mark);
    if (mark == 1) {
        $('#mark-1').html(star);
        $('#mark-2').html(empty);
        $('#mark-3').html(empty);
        $('#mark-4').html(empty);
        $('#mark-5').html(empty);
    }
    if (mark == 2) {
        $('#mark-1').html(star);
        $('#mark-2').html(star);
        $('#mark-3').html(empty);
        $('#mark-4').html(empty);
        $('#mark-5').html(empty);
    }
    if (mark == 3) {
        $('#mark-1').html(star);
        $('#mark-2').html(star);
        $('#mark-3').html(star);
        $('#mark-4').html(empty);
        $('#mark-5').html(empty);
    }
    if (mark == 4) {
        $('#mark-1').html(star);
        $('#mark-2').html(star);
        $('#mark-3').html(star);
        $('#mark-4').html(star);
        $('#mark-5').html(empty);
    }
    if (mark == 5) {
        $('#mark-1').html(star);
        $('#mark-2').html(star);
        $('#mark-3').html(star);
        $('#mark-4').html(star);
        $('#mark-5').html(star);
    }
}

function ban(isbn, visitor) {
    console.log(isbn);
    console.log(visitor);
    $.ajax({
        type: "POST",
        url: "/review/ban",
        data: {
            book: isbn,
            visitor: visitor
        },
        success: function (data, textStatus, jQxhr) {
            $('#review-container').html(data);
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function getPage(page) {
        var filterDto = JSON.parse($('#filter_input').text());
        filterDto.page = page;
        console.log(filterDto);

        $.ajax({
            type: "GET",
            url: "/review/all",
            data: { isbn: isbn,
                page: page},
            success: function (data, textStatus, jQxhr) {
                $('#review-container').html(data);
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });

}

function send_review() {
    mark = $('#mark-input').val();
    review = $('#review-input').val();
    isbn = $('#isbn-label').text();

    var reviewDto = {
        message: review,
        mark: mark,
        isbn: isbn
    }

    console.log(reviewDto);
    $.post('/review', reviewDto, function (response) {
        console.log(response);
        window.location.reload();
    });
}

var isbn = $('#isbn-label').text();
$.ajax({
    type: "GET",
    url: "/review/all",
    data: {
        isbn: isbn,
        page: 0},
    success: function (data, textStatus, jQxhr) {
        $('#review-container').html(data);

    },
    error: function (jqXhr, textStatus, errorThrown) {
        console.log(errorThrown);
    }
});
