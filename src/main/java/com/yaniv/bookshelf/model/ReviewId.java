package com.yaniv.bookshelf.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ReviewId implements Serializable {
    @OneToOne
    private Book book;
    @OneToOne
    private Visitor visitor;
}
