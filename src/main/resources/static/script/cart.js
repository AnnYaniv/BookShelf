function addProductToCart(isbn){
    console.log(isbn);
    $.post('/cart/add', {isbn: isbn}, function(response){
        console.log(response);
    });
}

function addElectronicVersionToCart(isbn){
    if(document.querySelector('input[name="version"]:checked').value == "ELECTRONIC") {
        console.log("Electronic");
        $.post('/cart/add-electronic', {isbn: isbn}, function (response) {
            console.log(response);
        });
    } else {
        addProductToCart(isbn);
    }
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

function delete_electronic(isbn) {
    console.log(isbn);
    $.ajax({
        type: "DELETE",
        url: "/cart/delete-electronic",
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
    setCookie("delete","delete",2);
    $.post('/cart/create', function(response){
        console.log(response);

    });
});

function setCookie(c_name, value, exdays) {
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var c_value = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
    document.cookie = c_name + "=" + c_value + ";path=/";
}
