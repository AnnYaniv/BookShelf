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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/book")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
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
            modelAndView.addObject("book", BookMapper.toDto(book));
            if (visitor.isPresent()) {
                modelAndView.addObject("isDownload", bookService.findElectronicByUserIdAndIsbn(visitor.get().getId(), isbn).isPresent());
                modelAndView.addObject("isReview", bookService.isBookBought(visitor.get().getId(), isbn));
            } else {
                modelAndView.addObject("isDownload", false);
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
            LOGGER.debug("book to edit - {}", optionalBook.get());
            modelAndView.addObject("bookdto", BookMapper.toDto(optionalBook.get()));
            modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
            Author author = new Author();
            author.setId(UUID.randomUUID().toString());
            modelAndView.addObject("author", author);
            modelAndView.addObject("authors", authorService.getAllSorted());
            return modelAndView;
        }
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('book:write')")
    public ModelAndView createBook() {
        ModelAndView modelAndView = new ModelAndView("book_edit");
        modelAndView.addObject("bookdto", BookMapper.toDto(new Book()));
        modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
        Author author = new Author();
        author.setId(UUID.randomUUID().toString());
        modelAndView.addObject("author", author);
        modelAndView.addObject("authors", authorService.getAllSorted());
        return modelAndView;
    }


    @SneakyThrows
    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('book:write')")
    public ModelAndView updateBook(@ModelAttribute BookDto bookdto) {
        Book book = BookMapper.toBook(bookdto);
        if (bookdto.getBook().isEmpty() && StringUtils.isBlank(bookdto.getBookUrl())) {
            LOGGER.info("Can't edit'");
            ModelAndView infoModel = new ModelAndView("information");
            infoModel.addObject("info", "Data not valid, check electronic book");
            return infoModel;
        }
        bookService.save(book);
        return editBook(book.getIsbn());
    }

    @GetMapping("/filter")
    public ModelAndView ajaxFilter(@RequestParam String name, @RequestParam int page) {
        LOGGER.debug("ajaxFilter method was called for-{}", name);
        LOGGER.debug("Books-{}", bookService.findByNameLike(name, page));
        LOGGER.debug("page-{}", page);
        Page<Book> bookPage = bookService.findByNameLike(name, page);
        if (bookPage.getTotalPages() - 1 < page) {
            page = bookPage.getTotalPages() - 1;
            bookPage = bookService.findByNameLike(name, page);
        }
        return bookSelectionModelCreator(bookPage, page, bookService);
    }

    @GetMapping("/getFilterBy")
    public ModelAndView getFilterBy(@ModelAttribute FilterDto filterDto) {
        LOGGER.debug("filter in getFilterBy: {}", filterDto);
        List<Book> books = bookService.filterBook(filterDto);
        if (books.isEmpty()) {
            filterDto.setPage(0);
            books = bookService.filterBook(filterDto);
        }
        return bookSelectionModelCreator(books, filterDto.getPage(), bookService);
    }

    @GetMapping("/get-by-user")
    public ModelAndView getBooksByUser(Principal principal, @RequestParam int page) {
        LOGGER.debug("get-by-user {}", page);
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        Page<Book> bookPage = bookService.findByUser(visitor.getId(), page);
        if ((bookPage.getTotalPages() - 1 < page) && (bookPage.getTotalPages() != 0)) {
            page = bookPage.getTotalPages() - 1;
            bookPage = bookService.findByUser(visitor.getId(), page);
        }
        return bookSelectionModelCreator(bookPage, page, bookService);
    }

    @GetMapping("/get-by-user-electronic")
    public ModelAndView getBooksByUserElectronic(Principal principal, @RequestParam int page) {
        LOGGER.debug("get-by-user-electronic {}", page);
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        Page<Book> bookPage = bookService.findByUserElectronic(visitor.getId(), page);
        if ((bookPage.getTotalPages() - 1 < page) && (bookPage.getTotalPages() != 0)) {
            page = bookPage.getTotalPages() - 1;
            bookPage = bookService.findByUserElectronic(visitor.getId(), page);
        }
        return bookSelectionModelCreator(bookPage, page, bookService);
    }

    @GetMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestParam String isbn, Principal principal) throws FileNotFoundException {
        String filename = Thread.currentThread().getContextClassLoader()
                .getResource("static/book/" + bookService.findElectronicByUserIdAndIsbn(
                        visitorService.findByEmail(principal.getName()).orElse(new Visitor()).getId(),
                        isbn).orElse(new Book()).getBookUrl()).getPath();

        File file = new File(filename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok().headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/txt")).body(resource);
    }

    public static ModelAndView bookSelectionModelCreator(Iterable<Book> books, int page, BookService bookService) {
        ModelAndView model = new ModelAndView("fragment/book_selection");
        List<BookReviewDto> bookReview = new ArrayList<>();
        books.forEach(book -> bookReview.add(
                BookReviewMapper.mapToDto(book, bookService.getAvgByBook(book.getIsbn()))));
        model.addObject("books", bookReview);
        model.addObject("page", page);
        return model;
    }
}
