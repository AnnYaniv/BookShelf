package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.repository.BookFilter;
import com.yaniv.bookshelf.repository.BookRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class BookService {
    private BookRepository bookRepository;
    private BookFilter bookFilter;

    @Autowired
    public BookService(BookRepository bookRepository, BookFilter bookFilter) {
        this.bookRepository = bookRepository;
        this.bookFilter = bookFilter;
    }

    public Iterable<Book> getAll(int page){
        return bookRepository.findAll(PageRequest.of(page, 9));
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public Optional<Book> findById(String id){
        return bookRepository.findByIsbn(id);
    }
    public Iterable<Book> findByNameLike(String name, int page){
        return bookRepository.findByNameLike(name, PageRequest.of(page, 9));
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
