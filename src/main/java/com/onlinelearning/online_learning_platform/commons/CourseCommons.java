package com.onlinelearning.online_learning_platform.commons;

import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseCommons {

    private CourseRepository courseRepository;

    public CourseCommons(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public Course checkCourseExist(Integer courseId) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found");
        }

        return optionalCourse.get();
    }
}
