package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {
}
