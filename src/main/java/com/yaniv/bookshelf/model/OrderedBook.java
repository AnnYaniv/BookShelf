package com.yaniv.bookshelf.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;

@NoArgsConstructor
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
    private int countInOrder;

    private double price;

    @ManyToOne
    private Invoice invoice;

    @PrePersist
    public void prePersist() {
        price = book.getPrice();
    }
}
