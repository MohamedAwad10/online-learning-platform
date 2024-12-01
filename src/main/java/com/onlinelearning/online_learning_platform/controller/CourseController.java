package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.course.request.CourseRequestDTO;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.response.CourseResponseDto;
import com.onlinelearning.online_learning_platform.dto.course.response.FullCourseDto;
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
    public ResponseEntity<CourseResponseDto> createCourse(@PathVariable Integer instructorId, @Valid @RequestBody CourseRequestDTO courseRequestDTO){

        CourseResponseDto courseDto = courseService.insert(instructorId, courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDto);
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable Integer courseId
            , @Valid @RequestBody CourseRequestDTO courseRequestDto){

        CourseResponseDto courseDto = courseService.update(courseId, courseRequestDto);
        return ResponseEntity.ok(courseDto);
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable int courseId){

        String message = courseService.delete(courseId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<FullCourseDto> findCourseById(@PathVariable Integer courseId){

        FullCourseDto course = courseService.findById(courseId);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<String> enrollInCourse(@PathVariable Integer courseId, @PathVariable Integer studentId){

        String message = courseService.enrollIn(courseId, studentId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> findMyEnrolledCourses(@PathVariable Integer studentId){

        List<AllCoursesDto> courses = courseService.findAllEnrolledCourses(studentId);
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
        }

        return ResponseEntity.ok(courses);
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

        String message = courseService.submitCourse(courseId);
        return ResponseEntity.ok(message);
    }
}