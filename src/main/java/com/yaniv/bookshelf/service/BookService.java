package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.repository.impl.BookFilter;
import com.yaniv.bookshelf.repository.BookRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class BookService {
    private static final int ITEMS_PER_PAGE = 9;
    private BookRepository bookRepository;
    private BookFilter bookFilter;

    @Autowired
    public BookService(BookRepository bookRepository, BookFilter bookFilter) {
        this.bookRepository = bookRepository;
        this.bookFilter = bookFilter;
    }

    public Optional<Book> findByUserIdAndIsbn(String userId, String isbn){
        return bookRepository.findByUserIdAndIsbn(userId, isbn);
    }

    public Optional<Book> findElectronicByUserIdAndIsbn(String userId, String isbn){
        return bookRepository.findElectronicByUserIdAndIsbn(userId, isbn);
    }

    public boolean isBookBought(String userId, String isbn){
        return bookRepository.findByUserIdAndIsbn(userId, isbn).isPresent();
    }

    public Page<Book> getAll(int page){
        return bookRepository.findAll(PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public Optional<Book> findById(String id){
        return bookRepository.findByIsbn(id);
    }
    public Page<Book> findByNameLike(String name, int page){
        return bookRepository.findByNameLike(name, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Page<Book> findByUser(String id, int page){
        return bookRepository.findByUser(id, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Page<Book> findByUserElectronic(String id, int page){
        return bookRepository.findByUserElectronic(id, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public List<Book> filterBook(FilterDto filterDto){
        BookFilter filter = bookFilter.clearQuery();
        if((filterDto.getGenre()!=null)&&(!filterDto.getGenre().isEmpty())){
            filter.filterByGenres(filterDto.getGenre());
        }
        double min = 0, max = Double.MAX_VALUE;
        if (filterDto.getMin() != null){
            min = filterDto.getMin();
        }
        if (filterDto.getMax() != null){
            max = filterDto.getMax();
        }
        filter.filterByPrice(min, max);

        switch (filterDto.getSort()) {
            case PRICE -> filter.sortByPrice();
            case POPULARITY -> filter.sortByPopularity();
            case PRICE_DESC -> filter.sortByPriceDesc();
            case POPULARITY_DESC -> filter.sortByPopularityDesc();
        }
        return filter.getResults(filterDto.getPage());
    }

    public double getAvgByBook(String isbn){
        double result = 0;
        Double review = bookRepository.getAvgByBook(isbn);
        if (review != null){
            result = review;
        }
        return Math.round(result);
    }
}
