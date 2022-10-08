package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {
    Optional<Book> findByName(String name);

    Optional<Book> findByIsbn(String s);
}
