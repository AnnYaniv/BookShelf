package com.yaniv.bookshelf.model.ids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewId implements Serializable {
    @Column(name = "book_isbn")
    private String book;

    @Column(name = "visitor_id")
    private String visitor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewId reviewId = (ReviewId) o;

        if (book != null ? !book.equals(reviewId.book) : reviewId.book != null) return false;
        return visitor != null ? visitor.equals(reviewId.visitor) : reviewId.visitor == null;
    }

    @Override
    public int hashCode() {
        int result = book != null ? book.hashCode() : 0;
        result = 31 * result + (visitor != null ? visitor.hashCode() : 0);
        return result;
    }
}
