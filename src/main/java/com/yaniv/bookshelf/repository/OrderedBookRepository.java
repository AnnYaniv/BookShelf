package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.OrderedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedBookRepository extends JpaRepository<OrderedBook, String> {
}
