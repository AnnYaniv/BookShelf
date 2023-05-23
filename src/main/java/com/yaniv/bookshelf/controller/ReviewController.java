package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.ReviewDto;
import com.yaniv.bookshelf.model.Review;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.Role;
import com.yaniv.bookshelf.service.ReviewService;
import com.yaniv.bookshelf.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public ModelAndView getReviews(@RequestParam String isbn, @RequestParam int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        visitorService.findByEmail(authentication.getName())
                .ifPresent(v -> isAdmin.set(v.getRole().equals(Role.ADMIN)));
        if (isAdmin.get()) {
            return getAllReviews(isbn, page);
        } else {
            return getAllNotBannedReviews(isbn, page);
        }
    }

    public ModelAndView getAllReviews(String isbn, int page){
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

    public ModelAndView getAllNotBannedReviews(String isbn, int page) {
        ModelAndView modelAndView = new ModelAndView("fragment/reviews_container");
        LOGGER.debug("Reviews for {}", isbn);
        Page<Review> reviewPage = reviewService.findByBookNotBanned(isbn,0);
        if (reviewPage.getTotalPages() < page)  {
            page = reviewPage.getTotalPages();
            reviewPage = reviewService.findByBookNotBanned(isbn,0);
        }
        reviewPage.forEach(review -> review.setVisitor(visitorService.findById(review.getVisitor())
                .orElse(new Visitor()).getUserName()));
        modelAndView.addObject("reviews", reviewPage);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @PostMapping("/ban")
    @PreAuthorize("hasAuthority('reviews:edit')")
    public ModelAndView banReview(@RequestParam String book, @RequestParam String visitor){
        Optional<Review> reviewOptional = reviewService.findByBookAndUsername(book, visitor);
        reviewOptional.ifPresent(review -> {
            review.setBanned(!review.isBanned());
            reviewService.save(review);
        });
        return getAllReviews(book, 1);
    }
}
