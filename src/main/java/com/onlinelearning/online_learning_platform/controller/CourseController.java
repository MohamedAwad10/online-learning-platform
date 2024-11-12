package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.CourseDTO;
import com.onlinelearning.online_learning_platform.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/online-learning/course")
public class CourseController {

    private CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCourse(@Valid @RequestBody CourseDTO courseDTO){
        return courseService.insert(courseDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable(name = "id") Integer courseId
            , @Valid @RequestBody CourseDTO courseDTO){
        return courseService.update(courseId, courseDTO);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable(name = "id") int courseId){
        return courseService.delete(courseId);
    }
}
