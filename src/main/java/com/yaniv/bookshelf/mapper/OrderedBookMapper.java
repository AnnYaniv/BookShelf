package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.dto.OrderedBookDto;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.OrderedBook;
import com.yaniv.bookshelf.model.enums.BookType;
import com.yaniv.bookshelf.service.BookService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderedBookMapper {
    private final BookService bookService;

    public OrderedBookMapper(BookService bookService) {
        this.bookService = bookService;
    }

    public OrderedBook toOrderedBook(Book book, int quantity, BookType type) {
        OrderedBook orderedBook = new OrderedBook();
        orderedBook.setBook(book);
        orderedBook.setPrice(book.getPrice());
        orderedBook.setBookType(type);
        orderedBook.setQuantity(quantity);
        return orderedBook;
    }

    public Set<OrderedBook> merge(Map<String, Integer> cart, Set<String> cartElect){
        Set<OrderedBook> books = new HashSet<>();
        if (cart != null) {
            books = cart.entrySet().stream().map(entry ->
                            toOrderedBook(bookService.findById(entry.getKey()).orElse(new Book()),
                                    entry.getValue(), BookType.PAPER))
                    .collect(Collectors.toSet());
        }
        if (cartElect != null) {
            for (String id : cartElect) {
                books.add(toOrderedBook(bookService.findById(id).orElse(new Book()),
                        1, BookType.ELECTRONIC));
            }
        }
        return books;
    }

    public OrderedBookDto toDto(OrderedBook orderedBook) {
        return new OrderedBookDto(orderedBook.getId(), orderedBook.getBook(),
                orderedBook.getQuantity(), orderedBook.getPrice(), orderedBook.getBookType(),
                bookService.getBookCover(orderedBook.getBook().getIsbn()));
    }
}
