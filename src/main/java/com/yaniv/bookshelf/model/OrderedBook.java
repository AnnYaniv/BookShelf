package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.BookType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class OrderedBook {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @OneToOne
    private Book book;

    @Min(1)
    private int quantity;

    private double price;

    @Enumerated(EnumType.STRING)
    private BookType bookType;

    @PrePersist
    public void prePersist() {
        price = book.getPrice();
    }
}
