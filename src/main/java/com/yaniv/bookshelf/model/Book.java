package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.Genre;
import com.yaniv.bookshelf.service.BookValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Book{
    @Id
    private String isbn;

    @NotBlank
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = { @JoinColumn(name = "book_isbn", referencedColumnName="isbn") },
            inverseJoinColumns = { @JoinColumn(name = "author_id", referencedColumnName="id")}
    )
    private Set<Author> author = new LinkedHashSet<>();

    @Type(type="text")
    private String annotation;
    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "book_genre", joinColumns = @JoinColumn(name = "isbn"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genre = new HashSet<>();
    private int year;
    private String publishingHouse;

    @Min(0)
    private int count;

    @Min(0)
    private double price;
    private int visited;

    private String cover;

    @NotBlank
    private String bookUrl;

    @PrePersist
    public void prePersist() {
        if((genre==null) || genre.isEmpty()){
            genre = asSet(Genre.NONE);
        }
        BookValidator.isValid(this);
    }

    public void incrementVisited(){
        visited++;
    }

    public void addAuthor(Author author) {
        this.author.add(author);
    }

}
