package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Favourite;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.service.BookService;
import com.yaniv.bookshelf.service.FavouriteService;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/favourite")
public class FavouriteController {

    private final static Logger LOGGER = LoggerFactory.getLogger(FavouriteController.class);
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
        if(visitorOptional.isPresent()){
            String favouriteId = favouriteService.getFavourite(visitorOptional.get()).getId();
            Iterable<Book> books = favouriteService.getBooks(0, favouriteId);
            ModelAndView modelAndView = new ModelAndView("favourite");
            modelAndView.addObject("books", books);
            modelAndView.addObject("page",0);
            return modelAndView;
        }
        return null;

    }

    @GetMapping("/pageable")
    public ModelAndView getFavouritePage(int page) {
        LOGGER.info("pageable");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Visitor> visitorOptional = visitorService.findByEmail( authentication.getName());
        if(visitorOptional.isPresent()){
            String favouriteId = favouriteService.getFavourite(visitorOptional.get()).getId();
            LOGGER.info("favouriteId: {}", favouriteId);
            Set<Book> books = StreamSupport.stream(favouriteService.getBooks(page, favouriteId).spliterator(),true).collect(Collectors.toSet());
            if(books.size() == 0){
                page--;
            }
            books.forEach(book -> LOGGER.info("Book {}", book));
            ModelAndView modelAndView = new ModelAndView("fragment/book_selection");
            modelAndView.addObject("books", books);
            modelAndView.addObject("page",page);
            return modelAndView;
        }
        return null;

    }

    @PostMapping("/add")
    public String addBook(String isbn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        LOGGER.info("ISBN: - {}", isbn);
        Optional<Book> optionalBook = bookService.findById(isbn);
        Favourite favourite = favouriteService.getFavourite(Objects.requireNonNull(
                visitorService.findByEmail(name).orElseGet(() -> null)));
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            favourite.addBook(book);
            favouriteService.save(favourite);
            LOGGER.info("Added - {}", book);
        }
        LOGGER.info("Book - {}", optionalBook);
        return favourite.getBooks() + "<br>";
    }
}