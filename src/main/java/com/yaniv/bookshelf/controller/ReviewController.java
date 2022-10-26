package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.ReviewDto;
import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.service.ReviewService;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@RequestMapping("/review")
@RestController
public class ReviewController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;
    private final VisitorService visitorService;


    @Autowired
    public ReviewController(ReviewService reviewService, VisitorService visitorService) {
        this.reviewService = reviewService;
        this.visitorService = visitorService;
    }

    @PostMapping()
    public String addReview(@ModelAttribute ReviewDto reviewDto, Principal principal){
        LOGGER.info("reviewDto {}, {}, {}",reviewDto.getIsbn(),
                reviewDto.getMark(), reviewDto.getMessage());

        return reviewService.save(new Review(reviewDto.getIsbn(),
                visitorService.findByEmail(principal.getName()).orElse(new Visitor()).getId(),
                reviewDto.getMark(),
                reviewDto.getMessage(), LocalDateTime.now())) + "";
    }

    @GetMapping("/all")
    public ModelAndView getAllReviews(@RequestParam String isbn, @RequestParam int page) {
        ModelAndView modelAndView = new ModelAndView("fragment/reviews_container");
        LOGGER.info("Reviews for {}", isbn);
        List<Review> reviews = StreamSupport.stream(reviewService.findByBook_Isbn(isbn,0).spliterator(), false).toList();
        if ((reviews.size() == 0) && (page != 0)) {
            page--;
            reviews = StreamSupport.stream(reviewService.findByBook_Isbn(isbn,0).spliterator(), false).toList();
        }
        reviews.forEach(review -> review.setVisitor(visitorService.findById(review.getVisitor())
                .orElse(new Visitor()).getUserName()));
        modelAndView.addObject("reviews", reviews);
        modelAndView.addObject("page", page);
        return modelAndView;
    }
}
