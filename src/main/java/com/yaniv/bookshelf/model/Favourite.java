package com.yaniv.bookshelf.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Favourite implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @OneToOne
    private Visitor visitor;

    @ManyToMany
    @ToString.Exclude
    private Set<Book> books;

    public Favourite(){
        books = new HashSet<>();
    }

    public boolean addBook(Book book) {
        return books.add(book);
    }

    public boolean removeBook(Book book) {
        return books.remove(book);
    }
}
