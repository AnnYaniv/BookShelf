package com.yaniv.bookshelf.controller.api;

import com.yaniv.bookshelf.bookutils.Chapter;
import com.yaniv.bookshelf.bookutils.FB2Utils;
import com.yaniv.bookshelf.dto.BookReviewDto;
import com.yaniv.bookshelf.mapper.BookReviewMapper;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookControllerApi {
    private final BookService bookService;

    @Autowired
    public BookControllerApi(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Book getBook(@RequestParam String isbn) {
        return bookService.findById(isbn).orElseGet(Book::new);
    }

    @GetMapping("/all")
    public List<BookReviewDto> getAll(int page) {
        return bookService.getAll(page).get().map(book -> BookReviewMapper.toDto(book,
                bookService.getAvgByBook(book.getIsbn()))
        ).toList();

    }

    @GetMapping("/all-pages")
    public int getAllTotalPages() {
        return bookService.getAll(0).getTotalPages();
    }

    @GetMapping("/cover")
    public String getCover(@RequestParam String isbn) {
        return bookService.getBookCover(isbn);
    }

    @GetMapping("/book-page")
    public List<String> getBookPage(@RequestParam String isbn, @RequestParam int page) {
        InputStream inputStream = new ByteArrayInputStream(bookService.getBookFile(isbn));
        return FB2Utils.getChapter(page, inputStream).getParagraphs();
    }

    @GetMapping("/book-pages-all")
    public List<String> getBookAllPages(@RequestParam String isbn){
        InputStream inputStream = new ByteArrayInputStream(bookService.getBookFile(isbn));
        return FB2Utils.getTitles(inputStream).stream().map(Chapter::getTitle).toList();
    }
}
