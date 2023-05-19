function onLoad() {
    const images = document.getElementsByClassName('downloadable');

    for (let image of images) {
        $.ajax({
            type: "GET",
            url: "/api/book/cover?isbn=" + image.id,
            success: function (data) {
                image.src = 'data:image/jpeg;charset=utf-8;base64,' + data;
                console.log(image.id + "download");
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
    }
}

window.onload = onLoad();