package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.BookReviewDto;
import com.yaniv.bookshelf.mapper.BookReviewMapper;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Favourite;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.service.BookService;
import com.yaniv.bookshelf.service.FavouriteService;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/favourite")
public class FavouriteController {
    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
    private final FavouriteService favouriteService;
    private final VisitorService visitorService;
    private final BookService bookService;

    @Autowired
    public FavouriteController(FavouriteService favouriteService, VisitorService visitorService, BookService bookService) {
        this.bookService = bookService;
        this.favouriteService = favouriteService;
        this.visitorService = visitorService;
    }

    @GetMapping
    public ModelAndView getFavourite(Principal principal) {
        Optional<Visitor> visitorOptional = visitorService.findByEmail(principal.getName());
        if (visitorOptional.isPresent()) {
            String favouriteId = favouriteService.getFavourite(visitorOptional.get()).getId();
            Iterable<Book> books = favouriteService.getBooks(0, favouriteId);
            ModelAndView modelAndView = new ModelAndView("favourite");
            List<BookReviewDto> bookReview = new ArrayList<>();
            books.forEach(book -> bookReview.add(BookReviewMapper.mapToDto(book, bookService.getAvgByBook(book.getIsbn()))));
            modelAndView.addObject("books", bookReview);
            modelAndView.addObject("page", 0);
            return modelAndView;
        }
        return null;

    }

    @GetMapping("/pageable")
    public ModelAndView getFavouritePage(int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Visitor> visitorOptional = visitorService.findByEmail(authentication.getName());
        if (visitorOptional.isPresent()) {
            String favouriteId = favouriteService.getFavourite(visitorOptional.get()).getId();
            Page<Book> bookPage = favouriteService.getBooks(page, favouriteId);
            if ((bookPage.getTotalPages() - 1  < page)&&(bookPage.getTotalPages()!=0)) {
                page = bookPage.getTotalPages() - 1;
                bookPage = favouriteService.getBooks(page, favouriteId);
            }
            return BookController.bookSelectionModelCreator(bookPage, page, bookService);
        }
        return null;
    }

    @PostMapping("/add")
    public String addBook(String isbn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Optional<Book> optionalBook = bookService.findById(isbn);
        Favourite favourite = favouriteService.getFavourite(Objects.requireNonNull(
                visitorService.findByEmail(name).orElseGet(() -> null)));
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (favourite.getBooks().contains(book)) {
                favourite.removeBook(book);
                LOGGER.info("Favourite removed - {}, user - {}", book, name);
            } else {
                favourite.addBook(book);
                LOGGER.info("Favourite added - {}, user -{}", book, name);
            }
            favouriteService.save(favourite);
        }
        return favourite.getBooks() + "<br>";
    }
}
