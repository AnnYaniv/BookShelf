function onPageFilter(page) {
    var st = document.querySelector('input[name="sort"]:checked').value;
    $.ajax({
        type: "GET",
        url: "/cart/manage",
        data: {page: page, status: st},
        success: function (data, textStatus, jQxhr) {
            console.log("pageable: " + page + " status: "+ st);
            $('#data-container').html(data +
                "<script src=\"script/cart_manage.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function cart_return() {
    $.ajax({
        type: "GET",
        url: "/cart/manage",
        data: {page: page, status: 'CREATING'},
        success: function (data, textStatus, jQxhr) {
            console.log("we get about");
            $('#data-container').html(data +
                "<script src=\"script/cart_manage.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function cart_status(status, id) {
    $.ajax({
        type: "PUT",
        url: "/cart/manage",
        data: {id: id, status: status},
        success: function (data, textStatus, jQxhr) {
            console.log("we get about");
            $('#data-container').html(data +
                "<script src=\"script/cart_manage.js\"></script>");
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}