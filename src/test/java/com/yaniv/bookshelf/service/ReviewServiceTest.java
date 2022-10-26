package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.model.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ReviewServiceTest {

    ReviewService target;

    @Autowired
    BookService bookService;

    @Autowired
    VisitorService visitorService;

    @BeforeEach
    void setup(@Autowired ReviewService reviewService){
        target = reviewService;
    }

    @Test
    void save() {
        Book book = bookService.findById("isbn-5").orElse(new Book());
        Visitor visitor = visitorService.findById("adm1").orElse(new Visitor());

        //Review review = new Review(book, visitor, 5, "good book", LocalDateTime.now());
        //target.save(review);
    }
}