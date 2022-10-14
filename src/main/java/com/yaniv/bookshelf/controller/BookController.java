package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.BookDto;
import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.mapper.BookMapper;
import com.yaniv.bookshelf.model.Author;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

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
            LOGGER.info("book to edit - {}",optionalBook.get());
            modelAndView.addObject("bookdto", BookMapper.toDto(optionalBook.get()));
            modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
            modelAndView.addObject("authors", authorService.getAll());
            Author author = new Author();
            author.setId(UUID.randomUUID().toString());
            modelAndView.addObject("author",author);
            return modelAndView;
        }
    }

    @GetMapping("/create")
    public ModelAndView createBook(){
        ModelAndView modelAndView = new ModelAndView("book_edit");
        modelAndView.addObject("bookdto", BookMapper.toDto(new Book()));
        modelAndView.addObject("genres", Arrays.stream(Genre.values()).toList());
        modelAndView.addObject("authors", authorService.getAll());
        Author author = new Author();
        author.setId(UUID.randomUUID().toString());
        modelAndView.addObject("author",author);
        return modelAndView;
    }

    @SneakyThrows
    @PostMapping("/edit")
    public String updateBook(@ModelAttribute BookDto bookdto) {
        Book book = BookMapper.toBook(bookdto);
        bookService.save(book);
        return  bookdto + "<br>" +
                book + "<br>";
    }

    @SneakyThrows
    @PostMapping("/create")
    public String createBook(@ModelAttribute BookDto bookdto) {
        Book book = BookMapper.toBook(bookdto);
        bookService.save(book);
        return  bookdto + "<br>" +
                book + "<br>";
    }

    @GetMapping("/filter")
    public ModelAndView ajaxFilter(@RequestParam String name, @RequestParam int page){
        LOGGER.info("ajaxFilter method was called for-{}", name);
        LOGGER.info("Books-{}",bookService.findByNameLike(name, page));
        LOGGER.info("page-{}",page);
        ModelAndView model = new ModelAndView("fragment/book_selection");
        List<Book> books = StreamSupport.stream(bookService.findByNameLike(name, page).spliterator(), false).toList();
        if((books.size() == 0)&&(page !=0 )){
            page--;
        }
        model.addObject("books",bookService.findByNameLike(name, page));
        model.addObject("page", page);
        return model;
    }

    @GetMapping("/getFilterBy")
    public ModelAndView getFilterBy(@ModelAttribute FilterDto filterDto){
        LOGGER.info("filter in getFilterBy: {}", filterDto);
        List<Book> books = (List<Book>)bookService.filterBook(filterDto);
        if((books.size() == 0)&&(filterDto.getPage() !=0 )){
            filterDto.setPage(filterDto.getPage()-1);
        }
        books.forEach(book -> LOGGER.info("Book {}",book));
        ModelAndView model = new ModelAndView("fragment/book_selection");
        model.addObject("books",bookService.filterBook(filterDto));
        model.addObject("page", filterDto.getPage());
        return model;
    }
}
