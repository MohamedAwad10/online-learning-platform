package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.InstructorDto;
import com.onlinelearning.online_learning_platform.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/online-learning/instructor")
public class InstructorController {

    private InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService){
        this.instructorService = instructorService;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> addInstructorRole(@PathVariable Integer userId){
        try {
            String message =  instructorService.addInstructorRole(userId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{instructorId}")
    public ResponseEntity<?> getInstructorById(@PathVariable Integer instructorId){
        try {
            InstructorDto instructor =  instructorService.getById(instructorId);
            return ResponseEntity.ok(instructor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
