package com.yaniv.bookshelf.dto;

import com.yaniv.bookshelf.model.ExtBook;
import com.yaniv.bookshelf.model.enums.Genre;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Value
public class BookDto {
    String isbn;
    String title;
    String annotation;
    int year;
    String publishingHouse;
    int count;
    double price;
    Integer visited;
    MultipartFile coverMultipart;
    MultipartFile[] bookMultipart;
    String coverByte;
    Set<ExtBook> extBooks;

    List<String> authors;
    Set<Genre> genre;

    @Override
    public String toString() {
        return "BookDto{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", annotation='" + annotation + '\'' +
                ", year=" + year +
                ", publishingHouse='" + publishingHouse + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", visited=" + visited +
                ", coverMultipart=" + (coverMultipart != null) +
                ", bookMultipart=" + (bookMultipart != null) +
                ", coverByte='" + (coverByte != null) + '\'' +
                '}';
    }
}
