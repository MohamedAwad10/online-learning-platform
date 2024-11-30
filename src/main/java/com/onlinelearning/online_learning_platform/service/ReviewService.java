package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.commons.Commons;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.exception.ReviewException;
import com.onlinelearning.online_learning_platform.mapper.ReviewMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.model.review.Review;
import com.onlinelearning.online_learning_platform.model.review.ReviewID;
import com.onlinelearning.online_learning_platform.repository.ReviewRepository;
import com.onlinelearning.online_learning_platform.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    private Commons commons;

    private ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, Commons commons
                        , ReviewMapper reviewMapper, StudentRepository studentRepository){
        this.reviewRepository = reviewRepository;
        this.commons = commons;
        this.reviewMapper = reviewMapper;
    }

    public Set<ReviewDto> getAll(Integer courseId) {

        commons.checkCourseExist(courseId);

        Set<Review> reviews = reviewRepository.findAllByCourseId(courseId);

        return reviews.stream().map(review -> reviewMapper.toDto(review)).collect(Collectors.toSet());
    }


    public ReviewDto addReview(Integer courseId, Integer studId, ReviewDto reviewDto) {

        Course course = commons.checkCourseExist(courseId);
        Student student = commons.checkStudentExist(studId);

        if(student.getReview() != null){
            throw new ReviewException("Student already reviewed this course");
        }

        Review review = reviewMapper.toEntity(reviewDto);
        review.setCourse(course);
        review.setStudent(student);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }

    public ReviewDto updateReview(Integer courseId, Integer reviewId, ReviewDto reviewDto) {

        Course course = commons.checkCourseExist(courseId);

        ReviewID reviewKey = new ReviewID(reviewId, course);
        Review review = checkReviewExist(reviewKey);

        review.setRate(reviewDto.getRate());
        review.setComment(reviewDto.getComment());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }

    public String delete(Integer courseId, Integer reviewId) {

        Course course = commons.checkCourseExist(courseId);

        ReviewID reviewKey = new ReviewID(reviewId, course);
        Review review = checkReviewExist(reviewKey);

        reviewRepository.delete(review);

        return "Review deleted successfully with ID: "+ review.getId();
    }

    public Review checkReviewExist(ReviewID reviewID) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewID);
        if(optionalReview.isEmpty()){
            throw new ReviewException("Review not found");
        }

        return optionalReview.get();
    }
}
