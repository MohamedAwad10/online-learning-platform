package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.course.CourseDto;
import com.onlinelearning.online_learning_platform.dto.course.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.InstructorDto;
import com.onlinelearning.online_learning_platform.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/online-learning/instructor/{instructorId}/")
public class InstructorController {

    private InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService){
        this.instructorService = instructorService;
    }

    @GetMapping
    public ResponseEntity<?> getInstructorById(@PathVariable Integer instructorId){
        try {
            InstructorDto instructor =  instructorService.getById(instructorId);
            return ResponseEntity.ok(instructor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("courses/")
    public ResponseEntity<?> getAllInstructorCourses(@PathVariable Integer instructorId){

        try {
            List<InstructorCoursesDto> allInstructorCourses = instructorService.getAllCourses(instructorId);
            if(allInstructorCourses.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
            }

            return ResponseEntity.ok(allInstructorCourses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("search/")
    public ResponseEntity<?> searchInstructorCourses(@PathVariable Integer instructorId
            , @RequestParam String keyword){

        try {
            Set<InstructorCoursesDto> courses = instructorService.searchCourses(instructorId, keyword);
            if(courses.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No results found");
            }

            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
