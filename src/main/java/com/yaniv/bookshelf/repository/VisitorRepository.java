package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Visitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends CrudRepository<Visitor, String> {
    @Override
    Optional<Visitor> findById(String s);
    Optional<Visitor> findByEmail(String s);
}
