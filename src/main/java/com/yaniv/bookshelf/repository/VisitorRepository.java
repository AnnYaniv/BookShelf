package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Visitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends CrudRepository<Visitor, String> {

}
