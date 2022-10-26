package com.yaniv.bookshelf.dto;

import lombok.Value;

@Value
public class ReviewDto {
    String message;
    Integer mark;
    String isbn;
}
