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

    @GetMapping("/{courseId}")
    public ResponseEntity<FullCourseDto> findCourseById(@PathVariable Integer courseId){

        FullCourseDto course = courseService.findById(courseId);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{courseId}/submit-for-review")
    public ResponseEntity<String> submitCourseForReview(@PathVariable Integer courseId){

        String message = courseService.submitCourse(courseId);
        return ResponseEntity.ok(message);
    }
}