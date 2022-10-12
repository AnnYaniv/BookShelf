package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.repository.BookFilter;
import com.yaniv.bookshelf.service.AuthorService;
import com.yaniv.bookshelf.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;

@RestController
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private final BookService bookService;
    private AuthorService authorService;

    @Autowired
    public MainController(BookService bookService, AuthorService authorService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public ModelAndView main(){
        FilterDto filterDto = new FilterDto(500d, 700d, null, null);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("filterDto", filterDto);
        modelAndView.addObject("books", bookService.getAll());
        return modelAndView;
    }

}
