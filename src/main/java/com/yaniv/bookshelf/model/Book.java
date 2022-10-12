package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
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
    @JoinTable(
            name = "book_author",
            joinColumns = { @JoinColumn(name = "book_isbn", referencedColumnName="isbn") },
            inverseJoinColumns = { @JoinColumn(name = "author_id", referencedColumnName="id")}
    )
    private Set<Author> author = new java.util.LinkedHashSet<>();

    @Type(type="text")
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

    public void addAuthor(Author author) {
        this.author.add(author);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                ", author=" + author +
//                ", annotation='" + annotation + '\'' +
                ", genre=" + genre +
                ", year=" + year +
                ", publishingHouse='" + publishingHouse + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", visited=" + visited +
                ", cover='" + cover + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                '}';
    }
}
