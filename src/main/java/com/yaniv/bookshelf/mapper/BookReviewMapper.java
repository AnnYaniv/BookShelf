package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.BookReviewDto;
import com.yaniv.bookshelf.model.Book;

public class BookReviewMapper {
    private BookReviewMapper() {
    }

    public static BookReviewDto toDto(Book book, double mark, String cover) {
        return new BookReviewDto(book.getIsbn(), book.getTitle(), cover,
                book.getPrice(), mark, book.getAuthors(), book.getGenre());
    }
}
