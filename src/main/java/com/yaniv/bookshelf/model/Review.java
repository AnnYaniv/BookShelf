package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.ids.ReviewId;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@IdClass(ReviewId.class)
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id @Column(name = "book_isbn")
    private String book;

    @Id @Column(name = "visitor_id")
    private String visitor;
    @Max(5)
    @Min(0)
    @Column(nullable = false)
    private int mark;

    private String message;

    private LocalDateTime time;

    private boolean isBanned;

    public Review(String book, String visitor, int mark, String message, LocalDateTime time) {
        this.book = book;
        this.visitor = visitor;
        this.mark = mark;
        this.message = message;
        this.time = time;
    }

    public Review(String book, String visitor, int mark, String message, LocalDateTime time, boolean isBanned) {
        this(book, visitor, mark, message, time);
        this.isBanned = isBanned;
    }

    @Override
    public String toString() {
        return "Review{" +
                "book=" + book +
                ", visitor=" + visitor +
                ", mark=" + mark +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", isBanned=" + isBanned +
                '}';
    }

    @PrePersist
    public void prePersist() {
        time = LocalDateTime.now();
    }
}
