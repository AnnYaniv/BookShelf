package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


public class BookMapper {

    @SneakyThrows
    public static Book toBook(BookDto dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setName(dto.getName());
        book.setPrice(dto.getPrice());
        book.setCount(dto.getCount());
        book.setAnnotation(dto.getAnnotation());
        book.setPublishingHouse(dto.getPublishingHouse());
        book.setYear(dto.getYear());

        MultipartFile cover = dto.getCover();
        if(!cover.isEmpty()) {
            String contentType = switch (Objects.requireNonNull(cover.getContentType())) {
                case "image/png" -> ".png";
                case "image/jpeg" -> ".jpeg";
                case "image/jpg" -> ".jpg";
                default -> ".data";
            };
            String name = UUID.randomUUID() + contentType;

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Files.copy(dto.getCover().getInputStream(),
                    Paths.get("src", "main", "resources", "static", "covers").resolve(
                            name));
            Files.copy(dto.getCover().getInputStream(),
                Path.of(Objects.requireNonNull(loader.getResource("static/covers")).toURI())
                    .resolve(name));
            book.setCover(name);
        } else {
            book.setCover(dto.getCoverUrl());
        }
        book.setAuthor(dto.getAuthorsIds().stream().map(authorId -> {
            Author author = new Author();
            author.setId(authorId);
            return author;
        }).collect(Collectors.toSet()));
        book.setGenre(dto.getGenre());
        book.setVisited(dto.getVisited());
        return book;
    }


    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getIsbn(), book.getName(), book.getAnnotation(), book.getYear(),
                book.getPublishingHouse(), book.getCount(), book.getPrice(), book.getVisited(),
                null, book.getCover() ,null, book.getAuthor().stream().map(Author::getId).toList(),
                book.getGenre()
        );
    }
}
