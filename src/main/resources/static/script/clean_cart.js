function get_cookie(name){
    return document.cookie.split(';').some(c => {
        return c.trim().startsWith(name + '=');
    });
}

function delete_cookie(name) {
    path = "/"
    if(get_cookie(name)) {
        document.cookie = name + "=" +
            ((path) ? ";path="+path:"")+
            ";expires=Thu, 01 Jan 1970 00:00:01 GMT";
    }
}

function clean() {
    if(get_cookie("delete")) {
        delete_cookie("delete");
        delete_cookie("invoice");
        delete_cookie("elversion");
    }
}