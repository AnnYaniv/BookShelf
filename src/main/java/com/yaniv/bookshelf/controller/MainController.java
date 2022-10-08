package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.service.AuthorService;
import com.yaniv.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

    private final BookService bookService;
    private AuthorService authorService;

    @Autowired
    public MainController(BookService bookService, AuthorService authorService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public ModelAndView main(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("books", bookService.getAll());
        return modelAndView;
    }
}
