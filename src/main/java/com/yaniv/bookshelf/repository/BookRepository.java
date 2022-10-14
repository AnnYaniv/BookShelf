package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByName(String name);
    Optional<Book> findByIsbn(String s);

    @Query("select book from Book book where book.name like concat('%', :bookName, '%')")
    Page<Book> findByNameLike(@Param("bookName") String name, Pageable pageable);

    @Override
    Page<Book> findAll(Pageable pageable);
}
