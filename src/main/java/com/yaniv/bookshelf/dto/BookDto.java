package com.yaniv.bookshelf.dto;

import com.yaniv.bookshelf.model.enums.Genre;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Value
public class BookDto {
    String isbn;
    String name;
    String annotation;
    int year;
    String publishingHouse;
    int count;
    double price;
    Integer visited;
    MultipartFile cover;
    MultipartFile book;
    String coverUrl;
    String bookUrl;
    String coverByte;

    List<String> authorsIds ;
    Set<Genre> genre;

    @Override
    public String toString() {
        return "BookDto{" +
                "isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                //", annotation='" + annotation + '\'' +
                ", year=" + year +
                ", publishingHouse='" + publishingHouse + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", visited=" + visited +
                //", cover=" + cover +
                ", coverUrl='" + coverUrl + '\'' +
                //", bookUrl=" + bookUrl +
                ", authorsIds=" + authorsIds +
                ", genre=" + genre +
                '}';
    }
}
