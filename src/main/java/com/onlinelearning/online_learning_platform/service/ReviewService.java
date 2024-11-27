package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.commons.CourseCommons;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
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

    private CourseCommons courseCommons;

    private ReviewMapper reviewMapper;

    private StudentRepository studentRepository;

    public ReviewService(ReviewRepository reviewRepository, CourseCommons courseCommons
                        , ReviewMapper reviewMapper, StudentRepository studentRepository){
        this.reviewRepository = reviewRepository;
        this.courseCommons = courseCommons;
        this.reviewMapper = reviewMapper;
        this.studentRepository = studentRepository;
    }

    public Set<ReviewDto> getAll(Integer courseId) throws Exception{

        courseCommons.checkCourseExist(courseId);

        Set<Review> reviews = reviewRepository.findAllByCourseId(courseId);

        return reviews.stream().map(review -> reviewMapper.toReviewDto(review)).collect(Collectors.toSet());
    }


    public ReviewDto addReview(Integer courseId, Integer studId, ReviewDto reviewDto) throws Exception{

        Course course = courseCommons.checkCourseExist(courseId);
        Student student = checkStudentExist(studId);

//        if(course.getReviews().stream().anyMatch(review -> review.getStudent().equals(student))){
//            throw new RuntimeException("Student already reviewed this course");
//        }
        if(student.getReview() != null){
            throw new RuntimeException("Student already reviewed this course");
        }

        Review review = reviewMapper.toEntity(reviewDto);
        review.setCourse(course);
        review.setStudent(student);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toReviewDto(savedReview);
    }

    public ReviewDto updateReview(Integer courseId, Integer reviewId, ReviewDto reviewDto) throws Exception {

        Course course = courseCommons.checkCourseExist(courseId);

        ReviewID reviewKey = new ReviewID(reviewId, course);
        Review review = checkReviewExist(reviewKey);

        review.setRate(reviewDto.getRate());
        review.setComment(reviewDto.getComment());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toReviewDto(updatedReview);
    }

    public String delete(Integer courseId, Integer reviewId) throws Exception{

        Course course = courseCommons.checkCourseExist(courseId);

        ReviewID reviewKey = new ReviewID(reviewId, course);
        Review review = checkReviewExist(reviewKey);

        reviewRepository.delete(review);

        return "Review deleted successfully";
    }

    public Student checkStudentExist(Integer studId) throws Exception{
        Optional<Student> optionalStudent = studentRepository.findById(studId);
        if(optionalStudent.isEmpty()){
            throw new Exception("User not found");
        }

        return optionalStudent.get();
    }

    public Review checkReviewExist(ReviewID reviewID) throws Exception{
        Optional<Review> optionalReview = reviewRepository.findById(reviewID);
        if(optionalReview.isEmpty()){
            throw new Exception("Review not found");
        }

        return optionalReview.get();
    }
}
