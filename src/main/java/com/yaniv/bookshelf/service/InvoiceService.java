package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.enums.OrderStatus;
import com.yaniv.bookshelf.repository.InvoiceRepository;
import com.yaniv.bookshelf.repository.OrderedBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");
    private final InvoiceRepository invoiceRepository;
    private final OrderedBookRepository orderedBookRepository;

    private static final int ITEMS_PER_PAGE = 12;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, OrderedBookRepository orderedBookRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderedBookRepository = orderedBookRepository;
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
}
