package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.CourseDTO;
import com.onlinelearning.online_learning_platform.enums.CourseStatus;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper){
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public ResponseEntity<String> insert(CourseDTO courseDTO) {

        Optional<Course> optionalCourse = courseRepository.findByTitle(courseDTO.getTitle());
        if(optionalCourse.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already exists with this title");
        }

        Course course = courseMapper.toCourseEntity(courseDTO);
        course.setStatus(CourseStatus.PENDING);

        courseRepository.save(course);
        return ResponseEntity.ok("Course created Successfully");
    }

    public ResponseEntity<String> update(Integer courseId, CourseDTO courseDTO) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        Course course = optionalCourse.get();
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setCategory(courseDTO.getCategory());
        course.setTags(courseDTO.getTags());

        courseRepository.save(course);
        return ResponseEntity.ok("Course updated successfully");
    }

    public ResponseEntity<String> delete(int courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        Course course = optionalCourse.get();
        courseRepository.delete(course);
        return ResponseEntity.ok("Course deleted successfully");
    }
}