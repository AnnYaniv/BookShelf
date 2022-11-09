package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.BookReviewDto;
import com.yaniv.bookshelf.model.Book;

public class BookReviewMapper {
    private BookReviewMapper() {
    }

    public static BookReviewDto mapToDto(Book book, double mark) {
        return new BookReviewDto(book.getCover(), book.getName(),
                book.getPrice(), book.getAuthor(), book.getGenre(), book.getIsbn(), mark);
    }
}
