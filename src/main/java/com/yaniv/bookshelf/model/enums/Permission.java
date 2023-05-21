package com.yaniv.bookshelf.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    BOOK_READ("book:read"), BOOK_WRITE("book:write"),
    AUTHOR_READ("author:read"), AUTHOR_WRITE("author:write"),
    INVOICE_WRITE("invoice:write"), INVOICE_CREATE("invoice:create");
    private final String permissionName;

    Permission(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String getAuthority() {
        return permissionName;
    }
}
