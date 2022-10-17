package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.service.AuthorService;
import com.yaniv.bookshelf.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public MainController(BookService bookService, AuthorService authorService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public ModelAndView main(){
        FilterDto filterDto = new FilterDto(500d, 700d, null, null, 0);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("filterDto", filterDto);
        modelAndView.addObject("books", bookService.getAll(0));
        modelAndView.addObject("page",0);
        return modelAndView;
    }

    @GetMapping("/pageable")
    public ModelAndView getPage(int page){
        ModelAndView modelAndView = new ModelAndView("fragment/book_selection");
        List<Book> books = StreamSupport.stream(bookService.getAll(page).spliterator(), false).toList();
        if(books.size() == 0){
            page--;
        }
        modelAndView.addObject("books", bookService.getAll(page));
        modelAndView.addObject("page",page);
        return modelAndView;
    }
}
