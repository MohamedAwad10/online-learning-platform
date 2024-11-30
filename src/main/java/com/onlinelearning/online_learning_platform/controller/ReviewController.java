package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/online-learning/courses/{courseId}/review")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCourseReviews(@PathVariable Integer courseId){

        Set<ReviewDto> reviews = reviewService.getAll(courseId);
        if(reviews.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
        }
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{studId}")
    public ResponseEntity<?> addReviewToCourse(@PathVariable Integer courseId, @PathVariable Integer studId
                                                , @Valid @RequestBody ReviewDto reviewDto){

        ReviewDto review = reviewService.addReview(courseId, studId, reviewDto);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReviewInCourse(@PathVariable Integer courseId
            , @PathVariable Integer reviewId, @Valid @RequestBody ReviewDto reviewDto){

        ReviewDto review = reviewService.updateReview(courseId, reviewId, reviewDto);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer courseId, @PathVariable Integer reviewId){

        String message = reviewService.delete(courseId, reviewId);
        return ResponseEntity.ok(message);
    }
}
