function cart_return() {
    $.ajax({
        type: "GET",
        url: "/cart/all",
        success: function (data, textStatus, jQxhr) {
            console.log("we get about");
            $('#data-container').html(data);
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function get_inv_by_id(id) {
    $.ajax({
        type: "GET",
        url: "/cart/by-id?id=" + id,
        success: function (data, textStatus, jQxhr) {
            console.log("we get inv");
            $('#data-container').html(data);
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

