package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.enums.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;

@SpringBootTest
@RunWith(SpringRunner.class)
class BookFilterTest {
    BookFilter target;

    @BeforeEach
    void setUp(@Autowired BookFilter target) {
        this.target = target;
    }

    @Test
    void fullFilterTest(){
        Iterable<Book> books = target.clearQuery()
//                .filterByPrice(500,700)
                .filterByGenres(asList(Genre.ADVENTURE, Genre.FANTASY))//TODO make this work
//                .sortByPrice()
                .getResults();
        books.forEach(System.out::println);
    }
}