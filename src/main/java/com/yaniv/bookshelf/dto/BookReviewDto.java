package com.yaniv.bookshelf.dto;

import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.enums.Genre;
import lombok.Value;

import java.util.Set;

@Value
public class BookReviewDto {
    String isbn;
    String title;
    String coverByte;
    double price;
    double mark;
    Set<Author> authors;
    Set<Genre> genre;
}
