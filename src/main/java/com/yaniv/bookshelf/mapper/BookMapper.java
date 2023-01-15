package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.service.DriveService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class BookMapper {
    private BookMapper() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger("mapper-log");

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
            book.setCover(saveDrive(cover, dto.getIsbn()));
        } else {
            book.setCover(dto.getCoverUrl());
        }
        MultipartFile bookFile = dto.getBook();
        if(!bookFile.isEmpty()) {
            book.setBookUrl(saveDrive(bookFile, dto.getIsbn()));
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

    @SneakyThrows
    private static String saveDrive(MultipartFile cover, String isbn){
        byte[] coverFile = cover.getBytes();
        DriveService.getInstance().upload(isbn, coverFile, DriveService.Folder.COVER, cover.getContentType());
        return isbn;
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

        ByteArrayOutputStream outputStream = DriveService.getInstance().downloadFile(book.getCover());
        String byteArr = outputStream != null ? Base64.getEncoder().encodeToString(outputStream.toByteArray()) : null;
        LOGGER.info("ByteArr {}", byteArr);
        return new BookDto(
                book.getIsbn(), book.getName(), book.getAnnotation(), book.getYear(),
                book.getPublishingHouse(), book.getCount(), book.getPrice(), book.getVisited(),
                null,null, book.getCover(),book.getBookUrl(),
                byteArr,book.getAuthor().stream().map(Author::getId).toList(),
                book.getGenre()
        );
    }
}
