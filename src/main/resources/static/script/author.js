function getPage(page) {
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
    document.getElementById("id").value = id;
    document.getElementById("firstName").value = name;
    document.getElementById("lastName").value = lastName;
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

(function () {

    var forms = document.getElementsByClassName('not-validated');
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                event.preventDefault();
                event.stopPropagation();
                const data ={
                    id: $('#id').val(),
                    firstName: $('#firstName').val(),
                    lastName: $('#lastName').val()
                }
                console.log("To send - " + data);
                $.ajax({
                    url: '/author',
                    method: "POST",
                    data: data,
                    success: function(data) {
                        console.log(data)
                        $('#data-container').html(data);
                    },
                    error: function (jqXhr, textStatus, errorThrown){
                        console.log("Error " + errorThrown)
                        console.log(errorThrown)
                    }
                });
            }, false)
        })
})()