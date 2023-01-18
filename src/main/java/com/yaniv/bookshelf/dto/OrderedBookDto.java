package com.yaniv.bookshelf.dto;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.enums.BookType;
import lombok.Value;

@Value
public class OrderedBookDto {
    String id;
    Book book;
    int quantity;
    double price;
    BookType bookType;
    String coverByte;
}
