package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.repository.impl.BookFilter;
import com.yaniv.bookshelf.repository.BookRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public Iterable<Book> getAll(int page){
        return bookRepository.findAll(PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public Optional<Book> findById(String id){
        return bookRepository.findByIsbn(id);
    }
    public Iterable<Book> findByNameLike(String name, int page){
        return bookRepository.findByNameLike(name, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Iterable<Book> findByUser(String id, int page){
        return bookRepository.findByUser(id, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Iterable<Book> findByUserElectronic(String id, int page){
        return bookRepository.findByUserElectronic(id, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Iterable<Book> filterBook(FilterDto filterDto){
        BookFilter filter = bookFilter.clearQuery();
        if((filterDto.getGenre()!=null)&&(!filterDto.getGenre().isEmpty())){
            filter.filterByGenres(filterDto.getGenre());
        }
        if ((filterDto.getMin()!= null)&&(filterDto.getMax()!=null)){
            filter.filterByPrice(filterDto.getMin(), filterDto.getMax());
        }
        switch (filterDto.getSort()) {
            case PRICE -> filter.sortByPrice();
            case POPULARITY -> filter.sortByPopularity();
        }
        return filter.getResults(filterDto.getPage());
    }
}
