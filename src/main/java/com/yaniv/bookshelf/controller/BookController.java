package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.dto.BookReviewDto;
import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.mapper.BookMapper;
import com.yaniv.bookshelf.mapper.BookReviewMapper;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Genre;
import com.yaniv.bookshelf.service.AuthorService;
import com.yaniv.bookshelf.service.BookService;
import com.yaniv.bookshelf.service.VisitorService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/book")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final AuthorService authorService;
    private final VisitorService visitorService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, VisitorService visitorService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.visitorService = visitorService;
    }

    @GetMapping
    public ModelAndView bookById(@RequestParam String isbn) {
        Optional<Book> optionalBook = bookService.findById(isbn);
        if (optionalBook.isEmpty()) {
            return new ModelAndView("forward:/");
        } else {
            Book book = optionalBook.get();
            book.incrementVisited();
            bookService.save(book);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Visitor> visitor = visitorService.findByEmail(authentication.getName());
            ModelAndView modelAndView = new ModelAndView("book_detail");
            modelAndView.addObject("book", book);
            if (visitor.isPresent()) {
                modelAndView.addObject("isReview", bookService.isBookBought(visitor.get().getId(), isbn));
            } else {
                modelAndView.addObject("isReview", false);
            }
            return modelAndView;
        }
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('book:write')")
    public ModelAndView editBook(@RequestParam String isbn) {
        Optional<Book> optionalBook = bookService.findById(isbn);
        if (optionalBook.isEmpty()) {
            return new ModelAndView("forward:/");
        } else {
            ModelAndView modelAndView = new ModelAndView("book_edit");
            LOGGER.info("book to edit - {}", optionalBook.get());
            modelAndView.addObject("bookdto", BookMapper.toDto(optionalBook.get()));
            modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
            modelAndView.addObject("authors", authorService.getAll());
            Author author = new Author();
            author.setId(UUID.randomUUID().toString());
            modelAndView.addObject("author", author);
            return modelAndView;
        }
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('book:write')")
    public ModelAndView createBook() {
        ModelAndView modelAndView = new ModelAndView("book_edit");
        modelAndView.addObject("bookdto", BookMapper.toDto(new Book()));
        modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
        modelAndView.addObject("authors", authorService.getAll());
        Author author = new Author();
        author.setId(UUID.randomUUID().toString());
        modelAndView.addObject("author", author);
        return modelAndView;
    }


    @SneakyThrows
    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('book:write')")
    public String updateBook(@ModelAttribute BookDto bookdto) {
        Book book = BookMapper.toBook(bookdto);
        bookService.save(book);
        return bookdto + "<br>" +
                book + "<br>";
    }

    @SneakyThrows
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('book:write')")
    public String createBook(@ModelAttribute BookDto bookdto) {
        Book book = BookMapper.toBook(bookdto);
        bookService.save(book);
        return bookdto + "<br>" +
                book + "<br>";
    }

    @GetMapping("/filter")
    public ModelAndView ajaxFilter(@RequestParam String name, @RequestParam int page) {
        LOGGER.info("ajaxFilter method was called for-{}", name);
        LOGGER.info("Books-{}", bookService.findByNameLike(name, page));
        LOGGER.info("page-{}", page);
        ModelAndView model = new ModelAndView("fragment/book_selection");
        List<Book> books = StreamSupport.stream(bookService.findByNameLike(name, page).spliterator(), false).toList();
        if ((books.size() == 0) && (page != 0)) {
            page--;
        }
        List<BookReviewDto> bookReview = new ArrayList<>();
        bookService.findByNameLike(name, page).forEach(book ->
                bookReview.add(
                        BookReviewMapper.mapToDto(book, bookService.getAvgByBook(book.getIsbn())
                        )));
        model.addObject("books", bookReview);
        model.addObject("page", page);
        return model;
    }

    @GetMapping("/getFilterBy")
    public ModelAndView getFilterBy(@ModelAttribute FilterDto filterDto) {
        LOGGER.info("filter in getFilterBy: {}", filterDto);
        List<Book> books = (List<Book>) bookService.filterBook(filterDto);
        if ((books.size() == 0) && (filterDto.getPage() != 0)) {
            filterDto.setPage(filterDto.getPage() - 1);
        }
        ModelAndView model = new ModelAndView("fragment/book_selection");
        List<BookReviewDto> bookReview = new ArrayList<>();
        bookService.filterBook(filterDto).forEach(book ->
                bookReview.add(
                        BookReviewMapper.mapToDto(book, bookService.getAvgByBook(book.getIsbn())
                        )));
        model.addObject("books", bookReview);
        model.addObject("page", filterDto.getPage());
        return model;
    }

    @GetMapping("/get-by-user")
    public ModelAndView getBooksByUser(Principal principal, @RequestParam int page) {
        LOGGER.info("get-by-user {}", page);
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        List<Book> books = StreamSupport.stream(bookService.findByUser(visitor.getId(), page).spliterator(), false).toList();
        if ((books.size() == 0) && (page != 0)) {
            page--;
            books = StreamSupport.stream(bookService.findByUser(visitor.getId(), page).spliterator(), false).toList();
        }
        ModelAndView model = new ModelAndView("fragment/book_selection");

        List<BookReviewDto> bookReview = new ArrayList<>();
        books.forEach(book ->
                bookReview.add(
                        BookReviewMapper.mapToDto(book, bookService.getAvgByBook(book.getIsbn())
                        )));
        model.addObject("books", bookReview);
        model.addObject("page", page);
        return model;
    }

    @GetMapping("/get-by-user-electronic")
    public ModelAndView getBooksByUserElectronic(Principal principal, @RequestParam int page) {
        LOGGER.info("get-by-user-electronic {}", page);
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        List<Book> books = StreamSupport.stream(bookService.findByUserElectronic(visitor.getId(), page).spliterator(), false).toList();
        if ((books.size() == 0) && (page != 0)) {
            page--;
            books = StreamSupport.stream(bookService.findByUserElectronic(visitor.getId(), page).spliterator(), false).toList();
        }
        ModelAndView model = new ModelAndView("fragment/book_selection");
        List<BookReviewDto> bookReview = new ArrayList<>();
        books.forEach(book ->
                bookReview.add(
                        BookReviewMapper.mapToDto(book, bookService.getAvgByBook(book.getIsbn())
                        )));
        model.addObject("books", bookReview);
        model.addObject("page", page);
        return model;
    }
}
