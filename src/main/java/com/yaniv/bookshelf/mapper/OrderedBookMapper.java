package com.yaniv.bookshelf.mapper;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.OrderedBook;
import com.yaniv.bookshelf.model.enums.BookType;
import com.yaniv.bookshelf.service.BookService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderedBookMapper {
    public static final OrderedBook toOrderedBook(Book book, int quantity, BookType type) {
        OrderedBook orderedBook = new OrderedBook();
        orderedBook.setBook(book);
        orderedBook.setPrice(book.getPrice());
        orderedBook.setBookType(type);
        orderedBook.setQuantity(quantity);
        return orderedBook;
    }

    public static final Set<OrderedBook> merge(Map<String, Integer> cart, Set<String> cartElect, BookService bookService){
        Set<OrderedBook> books = new HashSet<>();
        if (cart != null) {
            books = cart.entrySet().stream().map(entry ->
                            toOrderedBook(bookService.findById(entry.getKey()).orElse(new Book()),
                                    entry.getValue(), BookType.PAPER))
                    .collect(Collectors.toSet());
        }
        if (cartElect != null) {
            for (String id : cartElect) {
                books.add(OrderedBookMapper.toOrderedBook(bookService.findById(id).orElse(new Book()),
                        1, BookType.ELECTRONIC));
            }
        }
        return books;
    }
}
