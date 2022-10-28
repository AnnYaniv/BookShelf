function onPageFilter(page) {
    $.ajax({
        type: "GET",
        url: "/author/pageable",
        data: {page: page, firstName: $('#search-input').val()},
        success: function (data, textStatus, jQxhr) {
            console.log("pageable authors: " + page);
            $('#authorTable').html(data);
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function setId(id, name, lastName) {
    document.getElementById("authorId").value = id;
    document.getElementById("name").value = name;
    document.getElementById("l_name").value = lastName;
}

$('#search-input').change(function () {
    $.ajax({
        type: "GET",
        url: "/author/pageable",
        data: {page: 1, firstName: $('#search-input').val()},
        success: function (result) {
            $('#authorTable').html(result);
            console.log("search input changed" );
        }
    });
});