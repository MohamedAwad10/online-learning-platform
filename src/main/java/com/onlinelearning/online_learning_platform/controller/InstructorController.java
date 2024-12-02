package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.course.response.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.response.InstructorDto;
import com.onlinelearning.online_learning_platform.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/online-learning/instructor/{instructorId}")
public class InstructorController {

    private InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService){
        this.instructorService = instructorService;
    }

    @GetMapping
    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable Integer instructorId){

        InstructorDto instructor =  instructorService.getById(instructorId);
        return ResponseEntity.ok(instructor);
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getAllInstructorCourses(@PathVariable Integer instructorId){

        List<InstructorCoursesDto> allInstructorCourses = instructorService.getAllCourses(instructorId);
        if(allInstructorCourses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
        }

        return ResponseEntity.ok(allInstructorCourses);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchInstructorCourses(@PathVariable Integer instructorId
            , @RequestParam String keyword){

        Set<InstructorCoursesDto> courses = instructorService.searchCourses(instructorId, keyword);
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No results found");
        }

        return ResponseEntity.ok(courses);
    }
}
