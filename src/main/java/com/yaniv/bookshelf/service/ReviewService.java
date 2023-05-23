package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");
    private static final int ITEMS_PER_PAGE = 12;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review save(Review review) {
        LOGGER.debug("Saving review {}", review);
        return reviewRepository.save(review);
    }

    public Page<Review> findByBook_Isbn(String isbn, int page){
        return reviewRepository.findByBook(isbn, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Page<Review> findByBookNotBanned(String isbn, int page){
        return reviewRepository.findByBookAndBannedIsFalse(isbn, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Optional<Review> findByBookAndUsername(String book, String visitor){
        return reviewRepository.findByBookAndUsername(book, visitor);
    }

    public double getAvgByBook(String isbn){
        double result = 0;
        Double review = reviewRepository.getAvgByBook(isbn);
        if (review != null){
            result = review;
        }
        return Math.round(result);
    }
}
