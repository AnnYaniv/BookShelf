package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private static final int ITEMS_PER_PAGE = 12;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }
    public Iterable<Review> findByBook_Isbn(String isbn, int page){
        return reviewRepository.findByBook(isbn, PageRequest.of(page, ITEMS_PER_PAGE));
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
