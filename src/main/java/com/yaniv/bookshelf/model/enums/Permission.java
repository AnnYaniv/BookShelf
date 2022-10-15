package com.yaniv.bookshelf.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    BOOK_READ("book:read"), BOOK_WRITE("book:write"),
    AUTHOR_READ("author:read"), AUTHOR_WRITE("author:write");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }
}
