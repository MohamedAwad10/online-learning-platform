package com.onlinelearning.online_learning_platform.commons;

import com.onlinelearning.online_learning_platform.exception.CourseException;
import com.onlinelearning.online_learning_platform.exception.UserNotFoundException;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import com.onlinelearning.online_learning_platform.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Commons {

    private CourseRepository courseRepository;

    private StudentRepository studentRepository;

    public Commons(CourseRepository courseRepository, StudentRepository studentRepository){
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public Course checkCourseExist(Integer courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }

    public Student checkStudentExist(Integer studId) {
        Optional<Student> optionalStudent = studentRepository.findById(studId);
        if(optionalStudent.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        return optionalStudent.get();
    }
}
