package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
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
    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "book_genre", joinColumns = @JoinColumn(name = "isbn"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genre = new HashSet<>();
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
        if(genre.isEmpty()){
            genre.add(Genre.NONE);
        }
    }

    public void incrementVisited(){
        visited++;
    }
}
