package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findAllByBuyer_Email(String email);

    Optional<Invoice> findById(String id);
}
