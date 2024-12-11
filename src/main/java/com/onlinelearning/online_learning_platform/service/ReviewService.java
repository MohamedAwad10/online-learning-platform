package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.exception.ReviewException;
import com.onlinelearning.online_learning_platform.mapper.ReviewMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.model.review.Review;
import com.onlinelearning.online_learning_platform.model.review.ReviewID;
import com.onlinelearning.online_learning_platform.repository.ReviewRepository;

import com.onlinelearning.online_learning_platform.service.course.CourseValidator;
import com.onlinelearning.online_learning_platform.service.user.StudentValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    private CourseValidator courseValidator;

    private StudentValidator studentValidator;

    private ReviewMapper reviewMapper;

    private EnrollmentService enrollmentService;

    public ReviewService(ReviewRepository reviewRepository, CourseValidator courseValidator
            , StudentValidator studentValidator, ReviewMapper reviewMapper, EnrollmentService enrollmentService){
        this.reviewRepository = reviewRepository;
        this.courseValidator = courseValidator;
        this.studentValidator = studentValidator;
        this.reviewMapper = reviewMapper;
        this.enrollmentService = enrollmentService;
    }

    public Set<ReviewDto> getAll(Integer courseId) {

        courseValidator.checkCourseExist(courseId);
        Set<Review> reviews = reviewRepository.findAllByCourseId(courseId);
        return reviews.stream().map(review -> reviewMapper.toDto(review)).collect(Collectors.toSet());
    }

    @Transactional
    public ReviewDto addReview(Integer courseId, Integer studId, ReviewDto reviewDto) {

        Course course = courseValidator.checkCourseExist(courseId);
        Student student = studentValidator.checkStudentExist(studId);

        enrollmentService.checkStudentEnrolledInOrNot(student, course);
        checkIfReviewed(courseId, studId);

        Review review = reviewMapper.toEntity(reviewDto);
        review.setCourse(course);
        review.setStudent(student);

        // Ensure entity is saved and createdAt is populated
        Review savedReview = reviewRepository.saveAndFlush(review);
        return reviewMapper.toDto(savedReview);
    }

    @Transactional
    public ReviewDto updateReview(Integer courseId, Integer reviewId, ReviewDto reviewDto) {

        Course course = courseValidator.checkCourseExist(courseId);

        Review review = checkReviewExist(reviewId, course);
        review.setRate(reviewDto.getRate());
        review.setComment(reviewDto.getComment());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }

    @Transactional
    public String delete(Integer courseId, Integer reviewId) {

        Course course = courseValidator.checkCourseExist(courseId);
        Review review = checkReviewExist(reviewId, course);
        reviewRepository.delete(review);
        return "Review deleted successfully with ID: "+ review.getId();
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
