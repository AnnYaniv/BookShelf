package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.ReviewDto;
import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.service.ReviewService;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;

@RequestMapping("/review")
@RestController
public class ReviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
    private final ReviewService reviewService;
    private final VisitorService visitorService;


    @Autowired
    public ReviewController(ReviewService reviewService, VisitorService visitorService) {
        this.reviewService = reviewService;
        this.visitorService = visitorService;
    }

    @PostMapping()
    public String addReview(@ModelAttribute ReviewDto reviewDto, Principal principal){
        LOGGER.debug("reviewDto {}, {}, {}",reviewDto.getIsbn(),
                reviewDto.getMark(), reviewDto.getMessage());

        return reviewService.save(new Review(reviewDto.getIsbn(),
                visitorService.findByEmail(principal.getName()).orElse(new Visitor()).getId(),
                reviewDto.getMark(),
                reviewDto.getMessage(), LocalDateTime.now())) + "";
    }

    @GetMapping("/all")
    public ModelAndView getAllReviews(@RequestParam String isbn, @RequestParam int page) {
        ModelAndView modelAndView = new ModelAndView("fragment/reviews_container");
        LOGGER.debug("Reviews for {}", isbn);
        Page<Review> reviewPage = reviewService.findByBook_Isbn(isbn,0);
        if (reviewPage.getTotalPages() < page)  {
            page = reviewPage.getTotalPages();
            reviewPage = reviewService.findByBook_Isbn(isbn,0);
        }
        reviewPage.forEach(review -> review.setVisitor(visitorService.findById(review.getVisitor())
                .orElse(new Visitor()).getUserName()));
        modelAndView.addObject("reviews", reviewPage);
        modelAndView.addObject("page", page);
        return modelAndView;
    }
}
