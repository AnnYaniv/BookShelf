package com.yaniv.bookshelf.dto;


import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.enums.Genre;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.LinkedList;
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
    MultipartFile bookUrl;
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
                ", authorId="+ authorsIds +
//                ", cover=" + cover +
                '}';
    }
}
