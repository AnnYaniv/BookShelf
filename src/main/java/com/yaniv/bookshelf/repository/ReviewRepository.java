package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.model.ids.ReviewId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    Page<Review> findByBook(String isbn, Pageable pageable);

    @Query("select avg(rev.mark) from Review rev where rev.book = :isbn and rev.isBanned <> false")
    Double getAvgByBook(@Param("isbn") String isbn);

    @Query("from Review rev where rev.book = :isbn and rev.isBanned = false")
    Page<Review> findByBookAndBannedIsFalse(String isbn, Pageable pageable);

    @Query("select rev from Review rev join Visitor v on rev.visitor = v.id " +
            "where v.userName = :visitor and rev.book = :book")
    Optional<Review> findByBookAndUsername(String book, String visitor);
}
