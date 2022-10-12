package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.repository.BookFilter;
import com.yaniv.bookshelf.repository.BookRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Iterable<Book> getAll(){
        return bookRepository.findAll();
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public Optional<Book> findById(String id){
        return bookRepository.findByIsbn(id);
    }
    public Iterable<Book> findByNameLike(String name){
        return bookRepository.findByNameLike(name);
    }

    public BookFilter createQuery(){
        return bookFilter.clearQuery();
    }
}
