package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.ids.ReviewId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@IdClass(ReviewId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id @Column(name = "book_isbn")
    private String book;

    @Id @Column(name = "visitor_id")
    private String visitor;

//    @ManyToOne
//    private Book bookEntity;

    @Max(5)
    @Min(0)
    @Column(nullable = false)
    private int mark;

    private String message;

    private LocalDateTime time;

    @Override
    public String toString() {
        return "Review{" +
                "book=" + book +
                ", visitor=" + visitor +
                ", mark=" + mark +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }

    @PrePersist
    public void prePersist() {
        time = LocalDateTime.now();
    }
}
