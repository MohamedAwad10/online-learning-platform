package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.course.CourseCreationDTO;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.CourseDto;
import com.onlinelearning.online_learning_platform.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-learning/courses")
public class CourseController {

    private CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping("/{instructorId}/create")
    public ResponseEntity<?> createCourse(@PathVariable Integer instructorId, @Valid @RequestBody CourseCreationDTO courseCreationDTO){
        try {
            CourseCreationDTO courseDto = courseService.insert(instructorId, courseCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(courseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Integer courseId
            , @Valid @RequestBody CourseCreationDTO courseCreationDto){
        try {
            CourseCreationDTO courseDto = courseService.update(courseId, courseCreationDto);
            return ResponseEntity.ok(courseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable int courseId){

        try {
            String message = courseService.delete(courseId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> findCourseById(@PathVariable Integer courseId){
        try {
            CourseDto course = courseService.findById(courseId);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/pending-courses")
    public ResponseEntity<?> findAllPendingCourses(){
        List<AllCoursesDto> courses = courseService.findAllPending();
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No courses");
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/approved-courses")
    public ResponseEntity<?> findAllApprovedCourses(){
        List<AllCoursesDto> courses = courseService.findAllApproved();
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No courses");
        }
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/submit/{courseId}")
    public ResponseEntity<String> submitCourseForReview(@PathVariable Integer courseId){
        try {
            String message = courseService.submitCourse(courseId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}