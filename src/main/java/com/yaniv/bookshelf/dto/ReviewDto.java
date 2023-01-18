package com.yaniv.bookshelf.dto;

import lombok.Value;

@Value
public class ReviewDto {
    String isbn;
    String message;
    Integer mark;
}
