package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Book;
import org.apache.commons.lang3.StringUtils;

public abstract class BookValidator {
    private BookValidator() {}
    public static void isValid(Book book) {
        if (StringUtils.isBlank(book.getIsbn()) || (book.getPrice() <= 0) || (book.getCount() < 0)
                || !book.getBookUrls().isEmpty()) {
            throw new IllegalArgumentException("Book isn`t valid");
        }
    }
}
