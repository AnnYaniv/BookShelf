package com.yaniv.bookshelf.dto;

import lombok.Value;

@Value
public class VisitorDto {
    String userName;
    String email;
    String password;

    @Override
    public String toString() {
        return "VisitorDto{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
