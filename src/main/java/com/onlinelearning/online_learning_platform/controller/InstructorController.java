package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.course.response.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.dto.user.request.UserUpdateDto;
import com.onlinelearning.online_learning_platform.dto.user.response.InstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.response.UpdatedUserResponseDto;
import com.onlinelearning.online_learning_platform.service.InstructorService;
import com.onlinelearning.online_learning_platform.service.ReviewService;
import com.onlinelearning.online_learning_platform.service.UserService;
import jakarta.validation.Valid;
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

    private UserService userService;

    private ReviewService reviewService;

    @Autowired
    public InstructorController(InstructorService instructorService, UserService userService
            , ReviewService reviewService){
        this.instructorService = instructorService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/profile")
    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable Integer instructorId){
        return ResponseEntity.ok(instructorService.getById(instructorId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UpdatedUserResponseDto> updateInstructor(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable Integer instructorId){
        return ResponseEntity.ok(userService.update(userUpdateDto, instructorId));
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getAllInstructorCourses(@PathVariable Integer instructorId){

        List<InstructorCoursesDto> allInstructorCourses = instructorService.getAllCourses(instructorId);
        if(allInstructorCourses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
        }

        return ResponseEntity.ok(allInstructorCourses);
    }

    @GetMapping("/courses/search")
    public ResponseEntity<?> searchInstructorCourses(@PathVariable Integer instructorId
            , @RequestParam String keyword){

        Set<InstructorCoursesDto> courses = instructorService.searchCourses(instructorId, keyword);
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No results found");
        }

        return ResponseEntity.ok(courses);
    }
}
