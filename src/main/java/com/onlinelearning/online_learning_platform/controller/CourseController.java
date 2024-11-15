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
@RequestMapping("/api/online-learning/course")
public class CourseController {

    private CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping("/create/{instructor-id}")
    public ResponseEntity<String> createCourse(@PathVariable(name = "instructor-id")Integer instructorId, @Valid @RequestBody CourseCreationDTO courseCreationDTO){
        try {
            String message = courseService.insert(instructorId, courseCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable(name = "id") Integer courseId
            , @Valid @RequestBody CourseCreationDTO courseCreationDTO){
        try {
            String message = courseService.update(courseId, courseCreationDTO);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable(name = "id") int courseId){

        if(courseService.delete(courseId)){
            return ResponseEntity.ok("Course deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Course not found");

//        try {
//            String message = courseService.delete(courseId);
//            return ResponseEntity.ok(message);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCourseById(@PathVariable(name = "id") Integer courseId){
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

    @PostMapping("/submit/{id}")
    public ResponseEntity<String> submitCourseForReview(@PathVariable(name = "id") Integer courseId){
        try {
            String message = courseService.submitCourse(courseId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}