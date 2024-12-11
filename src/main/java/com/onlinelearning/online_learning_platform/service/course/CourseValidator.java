package com.onlinelearning.online_learning_platform.service.course;

import com.onlinelearning.online_learning_platform.dto.course.request.CourseRequestDTO;
import com.onlinelearning.online_learning_platform.exception.CourseException;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseValidator {

    private CourseRepository courseRepository;

    public CourseValidator(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course checkCourseExist(Integer courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }

    public Course checkApprovedCourseExist(Integer courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty() || !optionalCourse.get().getStatus().equals("APPROVED")){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }

    public Course checkPendingCourseExist(Integer courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty() || !optionalCourse.get().getStatus().equals("PENDING")){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }

    public void validateCourseTitleUniqueness(String title){
        if(courseRepository.findByTitle(title).isPresent()){
            throw new CourseException("Course already exists with this title");
        }
    }
}
