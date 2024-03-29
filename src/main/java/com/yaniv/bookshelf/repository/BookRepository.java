package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByIsbn(String s);

    @Query("select book from Book book where book.title like concat('%', :bookName, '%')")
    Page<Book> findByNameLike(@Param("bookName") String name, Pageable pageable);

    @Query("select distinct book from Invoice inv join inv.buyer visitor " +
            "join inv.booksInOrder bookOrder join bookOrder.book book where visitor.id = :userId " +
            "and inv.status <> 'DECLINED'")
    Page<Book> findByUser(@Param("userId") String id, Pageable pageable);

    @Query("select distinct book from Invoice inv join inv.buyer visitor " +
            "join inv.booksInOrder bookOrder join bookOrder.book book " +
            "where visitor.id = :userId and bookOrder.bookType = 'ELECTRONIC'" +
            "and inv.status <> 'DECLINED'")
    Page<Book> findByUserElectronic(@Param("userId") String id, Pageable pageable);

    @Query("select distinct book from Invoice inv join inv.buyer visitor " +
            "join inv.booksInOrder bookOrder join bookOrder.book book where " +
            "visitor.id = :userId and book.isbn = :isbn and inv.status <> 'DECLINED'")
    Optional<Book> findByUserIdAndIsbn(@Param("userId") String id, @Param("isbn") String isbn);

    @Query("select distinct book from Invoice inv join inv.buyer visitor " +
            "join inv.booksInOrder bookOrder join bookOrder.book book where " +
            "visitor.id = :userId and book.isbn = :isbn and bookOrder.bookType = 'ELECTRONIC' " +
            "and inv.status <> 'DECLINED'")
    Optional<Book> findElectronicByUserIdAndIsbn(@Param("userId") String id, @Param("isbn") String isbn);

    @Query("select avg(rev.mark) from Review rev where rev.book = :isbn")
    Double getAvgByBook(@Param("isbn") String isbn);

    @Query("from Book b order by b.visited desc")
    Page<Book> findAll(Pageable pageable);
}
