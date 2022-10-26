package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.model.ids.ReviewId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    Page<Review> findByBook(String isbn, Pageable pageable);

    @Query("select avg(rev.mark) from Review rev where rev.book = :isbn")
    Double getAvgByBook(@Param("isbn") String isbn);
}
