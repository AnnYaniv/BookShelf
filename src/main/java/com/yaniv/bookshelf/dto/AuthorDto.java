package com.yaniv.bookshelf.dto;

import lombok.NoArgsConstructor;
import lombok.Value;


@Value
public class AuthorDto {
    String id;
    String firstName;
    String lastName;
}
