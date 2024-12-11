package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.category.request.CategoryRequestDto;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryResponseDto;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.response.AllUsersDto;
import com.onlinelearning.online_learning_platform.service.CategoryService;
import com.onlinelearning.online_learning_platform.service.course.CourseService;
import com.onlinelearning.online_learning_platform.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-learning/admin")
public class AdminController {

    private UserService userService;

    private CourseService courseService;

    private CategoryService categoryService;

    public AdminController(UserService userService, CourseService courseService, CategoryService  categoryService){
        this.userService = userService;
        this.courseService = courseService;
        this.categoryService = categoryService;
    }

    // User management
    @GetMapping("/users")
    public ResponseEntity<List<AllUsersDto>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

//    @GetMapping("/users/{userId}") // i am confused should this go to user controller or here
//    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Integer userId){
//        return new ResponseEntity<>(userService.findById(userId), HttpStatus.FOUND);
//    }

    @PutMapping("/users/{userId}/promote-user")
    public ResponseEntity<String> setAdminRoleToUser(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.setAdminRole(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.delete(userId));
    }

    // Course management
    @GetMapping("/courses/pending")
    public ResponseEntity<?> getAllPendingCourses(){

        List<AllCoursesDto> courses = courseService.findAllPending();
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No pending courses found");
        }
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/courses/{courseId}/approve")
    public ResponseEntity<String> approveCourse(@PathVariable Integer courseId){
        return ResponseEntity.ok(courseService.approveCourse(courseId));
    }

    @PutMapping("/courses/{courseId}/reject")
    public ResponseEntity<String> rejectCourse(@PathVariable Integer courseId){
        return ResponseEntity.ok(courseService.rejectCourse(courseId));
    }

    // Category management
    @PostMapping("/category/create")
    public ResponseEntity<CategoryDtoWithoutCourses> addCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto){
        return new ResponseEntity<>(categoryService.create(categoryRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/category/{categoryId}/update")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Integer categoryId
            , @Valid @RequestBody CategoryRequestDto categoryRequestDto){
        return ResponseEntity.ok(categoryService.update(categoryId, categoryRequestDto));
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId){
        return ResponseEntity.ok(categoryService.delete(categoryId));
    }

}
