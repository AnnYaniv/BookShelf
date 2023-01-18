package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import lombok.SneakyThrows;

import java.util.stream.Collectors;

public class BookMapper {

    private BookMapper() {
    }

    @SneakyThrows
    public static Book toBook(BookDto dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setPrice(dto.getPrice());
        book.setCount(dto.getCount());
        book.setAnnotation(dto.getAnnotation());
        book.setPublishingHouse(dto.getPublishingHouse());
        book.setYear(dto.getYear());
        book.setGenre(dto.getGenre());
        book.setVisited(dto.getVisited());
        book.setAuthors(dto.getAuthors().stream().map(id -> {
            Author author = new Author();
            author.setId(id);
            return author;
        }).collect(Collectors.toSet()));
        return book;
    }

    public static BookDto toDto(Book book, String coverByte) {
        return new BookDto(
                book.getIsbn(), book.getTitle(), book.getAnnotation(), book.getYear(),
                book.getPublishingHouse(), book.getCount(), book.getPrice(), book.getVisited(),
                null, null,
                coverByte, null,
                book.getAuthors().stream().map(Author::getId).toList(),
                book.getGenre()
        );
    }
}
