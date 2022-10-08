package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Iterable<Author> getAll(){
        return authorRepository.findAll();
    }
}
