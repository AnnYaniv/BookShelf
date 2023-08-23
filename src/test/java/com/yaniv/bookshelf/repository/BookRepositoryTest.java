package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.ExtBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
class BookRepositoryTest {
    @Autowired
    BookRepository target;

    @Autowired
    ExtBookRepository extBookRepository;

    ExtBook ext1, ext2, ext3;
    Book book;

    @BeforeEach
    void setUp() {
        ext1 = new ExtBook("fb2", "1gOhPzLz9qF0rD-q-TNshkmY0ARdvJEBU");
        ext2 = new ExtBook("pdf", "1_3_lmSJR4JrQO92gLu7Wv44tssBSWc6J");
        ext3 = new ExtBook("epub", "235hisSJR4JrQO92gLu7Yu44tssBSWc6J");

        book = new Book();
        book.setIsbn(UUID.randomUUID().toString());
        book.setCount(23);
        book.setAnnotation("Annotation");
        book.setPrice(234.5f);
        book.setTitle("New Book");
        book.setYear(2023);
    }

    @Test
    void saveBook_WithFewFilesAttached() {
        extBookRepository.saveAll(List.of(ext1, ext2, ext3));

        book.setBookUrls(Set.of(ext1, ext2));

        target.save(book);
        target.findByIsbn(book.getIsbn()).ifPresentOrElse(
                result -> Assertions.assertEquals(book, result), Assertions::fail);
    }

    @Test
    void saveBook_WithOneFileAttached(){
        extBookRepository.saveAll(List.of(ext1, ext2, ext3));

        book.setBookUrls(Set.of(ext3));

        target.save(book);
        target.findByIsbn(book.getIsbn()).ifPresentOrElse(
                result -> Assertions.assertEquals(book, result), Assertions::fail);
    }

    @Test
    void saveBook_WithNoFileAttached(){
        extBookRepository.saveAll(List.of(ext1, ext2, ext3));

        target.save(book);
        target.findByIsbn(book.getIsbn()).ifPresentOrElse(
                result -> Assertions.assertEquals(book, result), Assertions::fail);
    }
}
