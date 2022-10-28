package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {
    Page<Author> findAll(Pageable pageable);

    Page<Author> findByFirstNameLike(String name, Pageable pageable);

    @Query("select a from Author a order by a.firstName")
    List<Author> getAll();
}
