package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");

    private final AuthorRepository authorRepository;

    private static final int ITEMS_PER_PAGE = 12;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author save(Author author) {
        LOGGER.info("Saving author {}", author);
        return authorRepository.save(author);
    }

    public Page<Author> getAll(int page){
        return authorRepository.findAll(PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public List<Author> getAllSorted() {
        return authorRepository.getAll();
    }

    public Page<Author> getByName(String name, int page){
        return authorRepository.findByFirstNameLike("%" + name + "%", PageRequest.of(page, ITEMS_PER_PAGE));
    }
}
