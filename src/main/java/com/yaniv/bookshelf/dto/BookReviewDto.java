package com.yaniv.bookshelf.dto;

import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.enums.Genre;
import lombok.Value;

import java.util.Set;

@Value
public class BookReviewDto {
    String cover;
    String name;
    double price;
    Set<Author> author;
    Set<Genre> genre;
    String isbn;
    double mark;
}
