package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/online-learning/enrollment")
public class EnrollmentController {

    private EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService){
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<String> enrollInCourse(@PathVariable Integer courseId, @PathVariable Integer studentId){
        String message = enrollmentService.enrollIn(courseId, studentId);
        return ResponseEntity.ok(message);
    }
}
