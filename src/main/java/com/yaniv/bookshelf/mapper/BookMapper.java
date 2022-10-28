package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class BookMapper {
    public static final Logger LOGGER = LoggerFactory.getLogger(BookMapper.class);
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
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        MultipartFile cover = dto.getCover();
        if(!cover.isEmpty()) {
            book.setCover(saveFile(cover,"covers" , loader));
        } else {
            book.setCover(dto.getCoverUrl());
        }
        MultipartFile bookFile = dto.getBook();
        if(!bookFile.isEmpty()) {
            book.setBookUrl(saveFile(bookFile,"book" , loader));
        } else {
            book.setBookUrl(dto.getBookUrl());
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

    private static String saveFile(MultipartFile cover, String folder, ClassLoader loader) throws IOException, URISyntaxException {
        String name = UUID.randomUUID() + getFileType(cover.getOriginalFilename());
        try {
            Files.copy(cover.getInputStream(),
                    Paths.get("src", "main", "resources", "static", folder).resolve(
                            name));
        } catch (Exception e) {
            LOGGER.warn("You not debug mode; src folder not exist");
        }
        Files.copy(cover.getInputStream(),
                Path.of(Objects.requireNonNull(loader.getResource("static/" + folder)).toURI())
                        .resolve(name));
        return name;
    }

    private static String getFileType(String filename){
        StringBuilder sb = new StringBuilder(".");
        Matcher matcher = Pattern.compile("[^\\\\]*\\.(\\w+)$").matcher(filename);
        while (matcher.find()) {
            sb.append(matcher.group(1));
        }
        return sb.toString();
    }

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getIsbn(), book.getName(), book.getAnnotation(), book.getYear(),
                book.getPublishingHouse(), book.getCount(), book.getPrice(), book.getVisited(),
                null,null, book.getCover(),book.getBookUrl() , book.getAuthor().stream().map(Author::getId).toList(),
                book.getGenre()
        );
    }
}
