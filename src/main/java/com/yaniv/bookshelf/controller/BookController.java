package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.mapper.BookMapper;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.enums.Genre;
import com.yaniv.bookshelf.service.AuthorService;
import com.yaniv.bookshelf.service.BookService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping
    public ModelAndView bookById(@RequestParam String isbn){
        Optional<Book> optionalBook = bookService.findById(isbn);
        if (optionalBook.isEmpty()){
            return new ModelAndView("forward:/");
        }
        else
        {
            ModelAndView modelAndView = new ModelAndView("book_detail");
            modelAndView.addObject("book", optionalBook.get());
            return modelAndView;
        }
    }

    @GetMapping("/edit")
    public ModelAndView editBook(@RequestParam String isbn){
        Optional<Book> optionalBook = bookService.findById(isbn);
        if (optionalBook.isEmpty()){
            return new ModelAndView("forward:/");
        }
        else
        {
            ModelAndView modelAndView = new ModelAndView("book_edit");
            modelAndView.addObject("bookdto", BookMapper.toDto(optionalBook.get()));
            modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
            modelAndView.addObject("authors", authorService.getAll());
            return modelAndView;
        }
    }

    @GetMapping("/create")
    public ModelAndView createBook(){
        ModelAndView modelAndView = new ModelAndView("book_edit");
        modelAndView.addObject("book", new Book());
        modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
        modelAndView.addObject("authors", authorService.getAll());
        return modelAndView;
    }

    @SneakyThrows
    @PostMapping("/edit")
    public String updateBook(@ModelAttribute BookDto bookdto) {
        return  bookdto + "<br>" +
                BookMapper.toBook(bookdto) + "<br>";
    }

    @SneakyThrows
    @PostMapping("/create")
    public String createBook(@ModelAttribute BookDto bookdto) {
        return  bookdto + "<br>" +
                BookMapper.toBook(bookdto) + "<br>";
    }
}
