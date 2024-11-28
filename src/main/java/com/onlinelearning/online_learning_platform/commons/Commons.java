package com.onlinelearning.online_learning_platform.commons;

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

    public Course checkCourseExist(Integer courseId) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found");
        }

        return optionalCourse.get();
    }

    public Student checkStudentExist(Integer studId) throws Exception{
        Optional<Student> optionalStudent = studentRepository.findById(studId);
        if(optionalStudent.isEmpty()){
            throw new Exception("User not found");
        }

        return optionalStudent.get();
    }
}
