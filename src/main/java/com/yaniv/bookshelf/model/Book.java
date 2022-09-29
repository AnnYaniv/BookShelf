package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    private String isbn;

    @NotBlank
    private String name;

    @ManyToMany
    private Set<Author> author;

    private String annotation;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private int year;
    private String publishingHouse;

//    @Column(columnDefinition = "0")
//    @Min(0)
    private int count;

//    @Column(columnDefinition = "0")
//    @Min(0)
    private double price;
    private int visited;

    private String cover;
    private String bookUrl;

    @PrePersist
    public void prePersist() {
        if(genre == null){
            genre = Genre.NONE;
        }
    }

    public void incrementVisited(){
        visited++;
    }
}
