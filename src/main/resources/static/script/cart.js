function addProductToCart(isbn){
    console.log(isbn);
    $.post('/cart/add', {isbn: isbn}, function(response){
        console.log(response);
    });
}

function on_change(price, isbn){
    var sum = $('#'+isbn+'input').val() * price;
    $('#'+isbn+'price').text(sum);
    $.ajax({
        type: "PUT",
        url: "/cart",
        data: {isbn: isbn, count: $('#'+isbn+'input').val()},
        success: function (data, textStatus, jQxhr) {
            window.location.reload();
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function delete_book(isbn) {
    console.log(isbn);
    $.ajax({
        type: "DELETE",
        url: "/cart",
        data: {isbn: isbn},
        success: function (data, textStatus, jQxhr) {
            window.location.reload();
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }

    });
}

$('#fav-a').on('click', function () {
    var isbn = $('#isbn-label').text();
    console.log(isbn);
    $.post('/favourite/add', {isbn: isbn}, function(response){
        console.log(response);
    });
});

$('#create-cart-button').on('click', function () {
    console.log("create inv");
    $.post('/cart/create', function(response){
        console.log(response);
    });
});