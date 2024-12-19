package com.onlinelearning.online_learning_platform.service.review;

import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.mapper.ReviewMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.model.review.Review;
import com.onlinelearning.online_learning_platform.repository.ReviewRepository;

import com.onlinelearning.online_learning_platform.service.enrollment.EnrollmentHandlerService;
import com.onlinelearning.online_learning_platform.service.enrollment.EnrollmentService;
import com.onlinelearning.online_learning_platform.service.course.CourseHandlerService;
import com.onlinelearning.online_learning_platform.service.user.StudentValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    private CourseHandlerService courseHandlerService;

    private StudentValidator studentValidator;

    private ReviewMapper reviewMapper;

    private EnrollmentHandlerService enrollmentHandlerService;

    private ReviewHandlerService reviewHandlerService;

    public ReviewService(ReviewRepository reviewRepository, CourseHandlerService courseHandlerService
            , StudentValidator studentValidator, ReviewMapper reviewMapper
            , EnrollmentHandlerService enrollmentHandlerService, ReviewHandlerService reviewHandlerService){
        this.reviewRepository = reviewRepository;
        this.courseHandlerService = courseHandlerService;
        this.studentValidator = studentValidator;
        this.reviewMapper = reviewMapper;
        this.enrollmentHandlerService = enrollmentHandlerService;
        this.reviewHandlerService = reviewHandlerService;
    }

    public Set<ReviewDto> getAll(Integer courseId) {
        courseHandlerService.checkCourseExist(courseId);
        Set<Review> reviews = reviewRepository.findAllByCourseId(courseId);
        return reviews.stream().map(review -> reviewMapper.toDto(review)).collect(Collectors.toSet());
    }

    @Transactional
    public ReviewDto addReview(Integer courseId, Integer studId, ReviewDto reviewDto) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        Student student = studentValidator.checkStudentExist(studId);

        enrollmentHandlerService.checkStudentEnrolledInOrNot(student, course);
        reviewHandlerService.checkIfReviewed(courseId, studId);

        Review review = reviewMapper.toEntity(reviewDto);
        review.setCourse(course);
        review.setStudent(student);

        // Ensure entity is saved and createdAt is populated
        Review savedReview = reviewRepository.saveAndFlush(review);
        return reviewMapper.toDto(savedReview);
    }

    @Transactional
    public ReviewDto updateReview(Integer courseId, Integer reviewId, ReviewDto reviewDto) {
        Course course = courseHandlerService.checkCourseExist(courseId);

        Review review = reviewHandlerService.checkReviewExist(reviewId, course);
        review.setRate(reviewDto.getRate());
        review.setComment(reviewDto.getComment());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }

    @Transactional
    public String delete(Integer courseId, Integer reviewId) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        Review review = reviewHandlerService.checkReviewExist(reviewId, course);
        reviewRepository.delete(review);
        return "Review deleted successfully with ID: "+ review.getId();
    }
}
