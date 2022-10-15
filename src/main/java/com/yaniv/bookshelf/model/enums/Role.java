package com.yaniv.bookshelf.model.enums;

import java.util.Set;

public enum Role {
    USER(Set.of(Permission.BOOK_READ)),
    ADMIN(Set.of(Permission.BOOK_READ, Permission.BOOK_WRITE, Permission.AUTHOR_WRITE, Permission.AUTHOR_READ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getAuthorities() {
        return permissions;
    }
}
