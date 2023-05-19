package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.BookReviewDto;
import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.mapper.BookReviewMapper;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    private final BookService bookService;

    @Autowired
    public MainController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ModelAndView index(){
        FilterDto filterDto = new FilterDto(500d, 700d, null, null, 0);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("filterDto", filterDto);
        List<BookReviewDto> bookReview = new ArrayList<>();
        bookService.getAll(0).forEach(book ->
                bookReview.add(BookReviewMapper.toDto(book,
                        bookService.getAvgByBook(book.getIsbn()))));
        modelAndView.addObject("books", bookReview);
        modelAndView.addObject("page",0);
        return modelAndView;
    }

    @GetMapping("/pageable")
    public ModelAndView getPage(int page){
        Page<Book> bookPage = bookService.getAll(page);
        if(bookPage.getTotalPages() < page){
            page = bookPage.getTotalPages();
            bookPage = bookService.getAll(page);
        }
        return BookController.bookSelectionModelCreator(bookPage, page, bookService);
    }
}
