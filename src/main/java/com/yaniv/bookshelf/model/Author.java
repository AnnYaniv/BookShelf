package com.yaniv.bookshelf.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    private String firstName;
    private String lastName;

//    @ManyToMany
//    @JoinTable(
//            name = "book_author",
//            joinColumns = { @JoinColumn(name = "author_id", referencedColumnName="id")},
//            inverseJoinColumns = { @JoinColumn(name = "book_isbn", referencedColumnName="isbn")}
//    )
//    private Set<Book> books = new java.util.LinkedHashSet<>();


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
