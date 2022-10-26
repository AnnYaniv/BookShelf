package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.enums.BookType;
import com.yaniv.bookshelf.model.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class StatusValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusValidator.class);

    public static Invoice validateStatus(Invoice invoice, OrderStatus newStatus, BookService bookService) {
        switch (newStatus) {
            case DECLINED -> {
                if (invoice.getStatus().equals(OrderStatus.CREATING)) {
                    LOGGER.info("Invoice {}, new status {}", invoice.getId(), newStatus);
                    invoice.setStatus(newStatus);
                }
            }
            case COMPLETED -> {
                if (invoice.getStatus().equals(OrderStatus.SHIPMENT)) {
                    LOGGER.info("Invoice {}, new status {}", invoice.getId(), newStatus);
                    invoice.setStatus(newStatus);
                }
            }
            case SHIPMENT -> {
                if (invoice.getStatus().equals(OrderStatus.CREATING)) {
                    AtomicBoolean canOrdered = new AtomicBoolean(true);
                    invoice.getBooksInOrder().forEach(
                            orderedBook -> {
                                canOrdered.set(canOrdered.get() &&
                                        ((orderedBook.getQuantity() < orderedBook.getBook().getCount()) ||
                                                orderedBook.getBookType().equals(BookType.ELECTRONIC))
                                );
                            });
                    if (canOrdered.get()) {
                        invoice.getBooksInOrder().forEach(orderedBook -> {
                            if (orderedBook.getBookType().equals(BookType.PAPER)) {
                                Book book = orderedBook.getBook();
                                book.setCount(book.getCount() - orderedBook.getQuantity());
                                bookService.save(book);
                            }
                        });
                        LOGGER.info("Invoice {}, new status {}", invoice.getId(), newStatus);
                        invoice.setStatus(newStatus);
                    }
                }
            }
        }
        return invoice;
    }
}
