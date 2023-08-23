package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.ExtBook;
import com.yaniv.bookshelf.model.ids.ExtBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtBookRepository extends JpaRepository<ExtBook, ExtBookId> {
}
