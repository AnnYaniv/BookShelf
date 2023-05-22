package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.bookutils.Chapter;
import com.yaniv.bookshelf.bookutils.FB2Utils;
import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.dto.BookReviewDto;
import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.mapper.BookMapper;
import com.yaniv.bookshelf.mapper.BookReviewMapper;
import com.yaniv.bookshelf.model.Author;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.ExtBook;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Genre;
import com.yaniv.bookshelf.service.AuthorService;
import com.yaniv.bookshelf.service.BookService;
import com.yaniv.bookshelf.service.VisitorService;
import lombok.SneakyThrows;
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

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
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
    public ModelAndView getBook(@RequestParam String isbn) {
        bookService.incrementVisited(isbn);
        final ModelAndView[] modelAndView = {new ModelAndView("book_detail")};
        Optional<Book> optionalBook = bookService.findById(isbn);
        optionalBook.ifPresentOrElse(book -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String[] userId = new String[1];
                    visitorService.findByEmail(authentication.getName()).ifPresentOrElse(
                            user -> userId[0] = user.getId(),
                            () -> userId[0] = ""
                    );
                    modelAndView[0].addObject("book", BookMapper.toDto(book));
                    modelAndView[0].addObject("isDownload", bookService.findElectronicByUserIdAndIsbn(userId[0], isbn).isPresent());
                    modelAndView[0].addObject("isReview", bookService.isBookBought(userId[0], isbn));
                },
                () -> modelAndView[0] = new ModelAndView("forward:/"));
        return modelAndView[0];
    }

    @GetMapping("/update")
    @PreAuthorize("hasAuthority('book:write')")
    public ModelAndView updateBook(@RequestParam String isbn) {
        return bookEditFill(new ModelAndView("book_edit"),
                bookService.findById(isbn).orElseGet(Book::new));
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('book:write')")
    public ModelAndView createBook() {
        return bookEditFill(new ModelAndView("book_create"), new Book());
    }

    @SneakyThrows
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('book:write')")
    public String updateBook(@ModelAttribute BookDto bookdto) {
        Book book = BookMapper.toBook(bookdto);
        bookService.setBookFiles(book, bookdto.getCoverMultipart(), bookdto.getBookMultipart());
        LOGGER.info("cover-{}, file-{}, {}",!bookdto.getCoverMultipart().isEmpty(),
                !bookdto.getBookMultipart().isEmpty(), book);
        bookService.save(book);
        return "Book saved successfully";
    }

    private ModelAndView bookEditFill(ModelAndView modelAndView, Book book) {
        modelAndView.addObject("bookdto", BookMapper.toDto(book));
        modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
        Author author = new Author();
        author.setId(UUID.randomUUID().toString());
        modelAndView.addObject("author", author);
        modelAndView.addObject("authors", authorService.getAllSorted());
        return modelAndView;
    }

    @GetMapping("/get-by-name")
    public ModelAndView getBooksByName(@RequestParam String name, @RequestParam int page) {
        Page<Book> bookPage = bookService.findByNameLike(name, page);
        if (bookPage.getTotalPages() - 1 < page) {
            page = bookPage.getTotalPages() - 1;
            bookPage = bookService.findByNameLike(name, page);
        }
        return bookSelectionModelCreator(bookPage, page, bookService);
    }

    @GetMapping("/get-by-filter")
    public ModelAndView getBooksByFilter(@ModelAttribute FilterDto filterDto) {
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
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        Page<Book> bookPage = bookService.findByUser(visitor.getId(), page);
        if ((bookPage.getTotalPages() - 1 < page) && (bookPage.getTotalPages() != 0)) {
            page = bookPage.getTotalPages() - 1;
            bookPage = bookService.findByUser(visitor.getId(), page);
        }
        return bookSelectionModelCreator(bookPage, page, bookService);
    }

    @GetMapping("/get-by-user-electronic")
    public ModelAndView getElectronicBooksByUser(Principal principal, @RequestParam int page) {
        LOGGER.debug("get-by-user-electronic {}", page);
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());
        Page<Book> bookPage = bookService.findByUserElectronic(visitor.getId(), page);
        if ((bookPage.getTotalPages() - 1 < page) && (bookPage.getTotalPages() != 0)) {
            page = bookPage.getTotalPages() - 1;
            bookPage = bookService.findByUserElectronic(visitor.getId(), page);
        }
        return bookSelectionModelCreator(bookPage, page, bookService);
    }

    @GetMapping("/read")
    @PreAuthorize("hasAuthority('book:read')")
    public ModelAndView readBook(@RequestParam String isbn, Principal principal){
        ExtBook ext = bookService.getExtBook(isbn);
        ModelAndView model = new ModelAndView("forward:/");
        if(visitorService.isSubscribed(principal.getName())) {
            if (ext.getExtension().equals("pdf")) {
                model = new ModelAndView("book_read_pdf");
                model.addObject("id", ext.getUrl());
            } else {
                model = new ModelAndView("book_read");
                model.addObject("titles", FB2Utils.getTitles(new ByteArrayInputStream(bookService.getBookFile(isbn)))
                        .stream().map(Chapter::getTitle).toList());
                model.addObject("chapter", FB2Utils.getChapter(1,
                        new ByteArrayInputStream(bookService.getBookFile(isbn))));
            }
        }
        return model;
    }


    @GetMapping("/drive")
    public ResponseEntity<Object> downloadDrive(@RequestParam String isbn, @RequestParam String ext, Principal principal) {
        Book book = bookService.findElectronicByUserIdAndIsbn(visitorService
                        .findByEmail(principal.getName()).orElse(new Visitor()).getId(), isbn).
                orElseGet(Book::new);
        byte[] file = bookService.getBookFileByUrl(book.getBookUrls().stream().filter(eb -> eb.getExtension().equals(ext)).findFirst().orElseThrow(
                () -> new IllegalArgumentException("Wrong file extension")).getUrl());
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s.%s\"",
                book.getIsbn(), bookService.getExtBook(book.getIsbn()).getExtension()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers)
                .contentLength(file.length).contentType(MediaType.parseMediaType(
                        new MimetypesFileTypeMap().getContentType("isbn." +
                                bookService.getExtBook(isbn).getExtension())))
                .body(resource);
    }

    public static ModelAndView bookSelectionModelCreator(Iterable<Book> books, int page, BookService bookService) {
        ModelAndView model = new ModelAndView("fragment/book_selection");
        List<BookReviewDto> bookReview = new ArrayList<>();
        books.forEach(book -> bookReview.add(BookReviewMapper.toDto(book,
                bookService.getAvgByBook(book.getIsbn()))));
        model.addObject("books", bookReview);
        model.addObject("page", page);
        return model;
    }
}
