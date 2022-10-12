package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.enums.Genre;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.swing.text.html.parser.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class BookFilter {
    EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Book> bookCriteria;
    private Root<Book> bookRoot;
    private SetJoin<Book,Genre> bookGenre;

    @Autowired
    public BookFilter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BookFilter clearQuery(){
        criteriaBuilder = entityManager.getCriteriaBuilder();
        bookCriteria = criteriaBuilder.createQuery(Book.class);
        bookRoot = bookCriteria.from(Book.class);
        bookGenre = bookRoot.joinSet("genre");
        return this;
    }

    public BookFilter filterByPrice(double lower, double upper){
        Predicate between = criteriaBuilder.between(bookRoot.get("price"), lower, upper);
        bookCriteria.where(between);
        return this;
    }

    public BookFilter filterByGenres(List<Genre> genres){
        bookCriteria.where(bookRoot.get("genre").in(genres));
        //bookGenre.in(bookGenre.get("genre").in(genres));

        return this;
    }

    public BookFilter sortByPrice(){
        bookCriteria.orderBy(criteriaBuilder.asc(bookRoot.get("price")));
        return this;
    }

    public BookFilter sortByPopularity(){
        bookCriteria.orderBy(criteriaBuilder.asc(bookRoot.get("visited")));
        return this;
    }

    public Iterable<Book> getResults() {
        TypedQuery<Book> query = entityManager.createQuery(bookCriteria);
        return query.getResultList();
    }
}
