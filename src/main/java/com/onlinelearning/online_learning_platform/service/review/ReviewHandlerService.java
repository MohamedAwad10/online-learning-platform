package com.onlinelearning.online_learning_platform.service.review;

import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.exception.ReviewException;
import com.onlinelearning.online_learning_platform.mapper.ReviewMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.review.Review;
import com.onlinelearning.online_learning_platform.model.review.ReviewID;
import com.onlinelearning.online_learning_platform.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewHandlerService {

    private ReviewRepository reviewRepository;

    private ReviewMapper reviewMapper;

    public ReviewHandlerService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public Review checkReviewExist(Integer reviewId, Course course) {
        ReviewID reviewKey = new ReviewID(reviewId, course);
        Optional<Review> optionalReview = reviewRepository.findById(reviewKey);
        if(optionalReview.isEmpty()){
            throw new ReviewException("Review not found");
        }
        return optionalReview.get();
    }

    public Set<ReviewDto> getReviewsDto(Set<Review> reviews){
        return reviews.stream()
                .map(review -> reviewMapper.toDto(review)).collect(Collectors.toSet());
    }

    public void checkIfReviewed(Integer courseId, Integer studId){
        Optional<Review> optionalReview = reviewRepository.findByCourseIdAndStudentId(courseId, studId);
        if(optionalReview.isPresent()){
            throw new ReviewException("Student already reviewed this course");
        }
    }
}
