package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.response.SearchedInstructorDto;
import com.onlinelearning.online_learning_platform.service.category.CategoryService;
import com.onlinelearning.online_learning_platform.service.course.CourseService;
import com.onlinelearning.online_learning_platform.service.user.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-learning/home")
public class HomeController {

    private InstructorService instructorService;

    private CourseService courseService;

    private CategoryService categoryService;

    public HomeController(InstructorService instructorService, CourseService courseService
            , CategoryService categoryService){
        this.instructorService = instructorService;
        this.courseService = courseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/search-instructor")
    public ResponseEntity<?> searchAboutInstructor(@RequestParam String keyword){
        List<SearchedInstructorDto> instructors = instructorService.searchInstructor(keyword);
        if(instructors.isEmpty()){
            return new ResponseEntity<>("Sorry, we couldn't find any results for "+ keyword, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(instructors);
    }

    @GetMapping("/{studentId}/my-courses/learning")
    public ResponseEntity<?> findMyEnrolledCourses(@PathVariable Integer studentId){

        List<AllCoursesDto> courses = courseService.findAllEnrolledCourses(studentId);
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
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

    @GetMapping("/search-courses")
    public ResponseEntity<?> searchCourses(@RequestParam String keyword){

        List<AllCoursesDto> courseList = courseService.searchCourses(keyword);
        if(courseList.isEmpty()){
            return new ResponseEntity<>("Sorry, we couldn't find any results for "+ keyword, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(courseList);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(){
        List<CategoryDtoWithoutCourses> categories = categoryService.allCategories();
        if(categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
        }

        return ResponseEntity.ok(categories);
    }
}
