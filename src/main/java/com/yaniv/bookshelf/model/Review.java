package com.yaniv.bookshelf.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@IdClass(ReviewId.class)
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @OneToOne
    private Book book;

    @Id
    @OneToOne
    private Visitor visitor;

    @Max(5)
    @Min(0)
    @Column(nullable = false)
    private int mark;

    private String message;
}
