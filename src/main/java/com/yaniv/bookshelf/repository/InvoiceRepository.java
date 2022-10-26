package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Page<Invoice> findAllByBuyer_EmailOrderByOrderedAtDesc(String email, Pageable pageable);

    Page<Invoice> findByStatusOrderByOrderedAtDesc( OrderStatus status, Pageable pageable);

    Optional<Invoice> findById(String id);
}
