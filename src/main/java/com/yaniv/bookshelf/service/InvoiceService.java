package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.repository.InvoiceRepository;
import com.yaniv.bookshelf.repository.OrderedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final OrderedBookRepository orderedBookRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, OrderedBookRepository orderedBookRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderedBookRepository = orderedBookRepository;
    }

    public void save(Invoice invoice) {
        orderedBookRepository.saveAll(invoice.getBooksInOrder());
        invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllByEmail(String email) {
        return invoiceRepository.findAllByBuyer_Email(email);
    }

    public Optional<Invoice> findById(String id) {
        return invoiceRepository.findById(id);
    }
}
