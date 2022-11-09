package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.enums.BookType;
import com.yaniv.bookshelf.model.enums.OrderStatus;
import com.yaniv.bookshelf.repository.BookRepository;
import com.yaniv.bookshelf.repository.InvoiceRepository;
import com.yaniv.bookshelf.repository.OrderedBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");
    private final InvoiceRepository invoiceRepository;
    private final OrderedBookRepository orderedBookRepository;
    private final BookRepository bookRepository;

    private static final int ITEMS_PER_PAGE = 12;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, OrderedBookRepository orderedBookRepository, BookRepository bookRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderedBookRepository = orderedBookRepository;
        this.bookRepository = bookRepository;
    }

    public void save(Invoice invoice) {
        LOGGER.info("Saving invoice {}", invoice);
        orderedBookRepository.saveAll(invoice.getBooksInOrder());
        invoiceRepository.save(invoice);
    }

    public Page<Invoice> getAllByEmail(String email, int page) {
        return invoiceRepository.findAllByBuyer_EmailOrderByOrderedAtDesc(email, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Page<Invoice> getAllByStatus(OrderStatus status, int page) {
        return invoiceRepository.findByStatusOrderByOrderedAtDesc(status, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Optional<Invoice> findById(String id) {
        return invoiceRepository.findById(id);
    }

    public Invoice changeStatus(Invoice invoice, OrderStatus orderStatus){
        OrderStatus prevStatus = invoice.getStatus();
        OrderStatus newStatus = switch (orderStatus) {
            case CREATING -> invoice.getState().onCreating();
            case DECLINED -> invoice.getState().onDeclined();
            case COMPLETED -> invoice.getState().onCompleted();
            case SHIPMENT -> invoice.getState().onShipment();
        };
        if((prevStatus != newStatus) && (newStatus == OrderStatus.SHIPMENT)){
            bookQuantityUpdate(invoice);
        }
        LOGGER.debug("Previous Status-{}; New Status-{}",prevStatus,newStatus);
        return invoice;
    }

    private void bookQuantityUpdate(Invoice invoice){
        invoice.getBooksInOrder().forEach(orderedBook -> {
            if (orderedBook.getBookType().equals(BookType.PAPER)) {
                Book book = orderedBook.getBook();
                book.setCount(book.getCount() - orderedBook.getQuantity());
                bookRepository.save(book);
            }
        });
    }
}
