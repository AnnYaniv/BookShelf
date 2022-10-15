package com.yaniv.bookshelf.security;

import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Permission;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {
    private final String username;
    private final String password;
    private final List<Permission> authorities;
    private final boolean isActive;

    public SecurityUser(String username, String password, List<Permission> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromVisitor(Visitor visitor) {
        return new org.springframework.security.core.userdetails.User(
                visitor.getEmail(), visitor.getPassword(),
                true,
                true,
                true,
                true,
                visitor.getRole().getAuthorities()
        );
    }
}

