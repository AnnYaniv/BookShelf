package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.enums.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookFilter {
    BookFilter clearQuery();

    List<Book> getResults(int page);

    BookFilter filterByPrice(double lower, double upper);

    BookFilter filterByGenres(List<Genre> genres);

    BookFilter sortByPrice();

    BookFilter sortByPopularity();

    BookFilter sortByPriceDesc();

    BookFilter sortByPopularityDesc();
}
