package com.yaniv.bookshelf.repository.impl;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.enums.Genre;
import com.yaniv.bookshelf.repository.BookFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookFilterImpl implements BookFilter {
    private static final int PAGE_SIZE = 9;
    private final EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Book> bookCriteria;
    private Root<Book> bookRoot;
    private SetJoin<Book,Genre> bookGenre;
    private List<Predicate> predicates;

    @Autowired
    public BookFilterImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BookFilter clearQuery(){
        predicates = new ArrayList<>();
        criteriaBuilder = entityManager.getCriteriaBuilder();
        bookCriteria = criteriaBuilder.createQuery(Book.class);
        bookRoot = bookCriteria.from(Book.class);
        bookGenre = bookRoot.joinSet("genre");
        return this;
    }

    @Override
    public BookFilter filterByPrice(double lower, double upper){
        Predicate between = criteriaBuilder.between(bookRoot.get("price"), lower, upper);
        predicates.add(between);
        return this;
    }

    @Override
    public BookFilter filterByGenres(List<Genre> genres){
        predicates.add(bookGenre.in(genres));
        return this;
    }

    @Override
    public BookFilter sortByPrice(){
        bookCriteria.orderBy(criteriaBuilder.asc(bookRoot.get("price")));
        return this;
    }

    @Override
    public BookFilter sortByPopularity(){
        bookCriteria.orderBy(criteriaBuilder.asc(bookRoot.get("visited")));
        return this;
    }

    @Override
    public BookFilter sortByPriceDesc(){
        bookCriteria.orderBy(criteriaBuilder.desc(bookRoot.get("price")));
        return this;
    }

    @Override
    public BookFilter sortByPopularityDesc(){
        bookCriteria.orderBy(criteriaBuilder.desc(bookRoot.get("visited")));
        return this;
    }

    @Override
    public List<Book> getResults(int page) {
        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        if(predicateArray.length > 0) {
            bookCriteria.where(predicateArray);
        }
        bookCriteria.distinct(true);
        TypedQuery<Book> query = entityManager.createQuery(bookCriteria);
        query.setFirstResult(page * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }
}
