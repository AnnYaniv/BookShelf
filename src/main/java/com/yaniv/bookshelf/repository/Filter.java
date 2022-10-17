package com.yaniv.bookshelf.repository;

public interface Filter<E> {
    Filter<E> clearQuery();
    Iterable<E> getResults(int page);
}
