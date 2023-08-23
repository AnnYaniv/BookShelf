package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.Genre;
import com.yaniv.bookshelf.service.BookValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.*;


@Entity
@Getter
@Setter
@ToString
public class Book{
    public Book() {
        genre = new HashSet<>();
    }

    @Id
    private String isbn;

    @NotBlank
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = {@JoinColumn(name = "book_isbn", referencedColumnName = "isbn")},
            inverseJoinColumns = {@JoinColumn(name = "author_id", referencedColumnName = "id")}
    )
    private Set<Author> authors = new LinkedHashSet<>();

    @Type(type="text")
    private String annotation;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "book_genre", joinColumns = @JoinColumn(name = "isbn"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genre;

    private int year;
    private String publishingHouse;

    @Min(0)
    private int count;

    @Min(0)
    private double price;
    private int visited;

    private String coverUrl;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "isbn", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Set<ExtBook> bookUrls = new HashSet<>();

    @PrePersist
    public void prePersist() {
        BookValidator.isValid(this);
    }

    public void incrementVisited(){
        visited++;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year && count == book.count && Double.compare(book.price, price) == 0 && visited == book.visited && Objects.equals(isbn, book.isbn) && Objects.equals(title, book.title) && Objects.equals(authors, book.authors) && Objects.equals(annotation, book.annotation) && Objects.equals(genre, book.genre) && Objects.equals(publishingHouse, book.publishingHouse) && Objects.equals(coverUrl, book.coverUrl) && Objects.equals(bookUrls, book.bookUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, authors, annotation, genre, year, publishingHouse, count, price, visited, coverUrl, bookUrls);
    }
}
