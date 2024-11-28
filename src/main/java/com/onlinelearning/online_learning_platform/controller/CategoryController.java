package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.category.AllCategoriesDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryWithoutCoursesDto;
import com.onlinelearning.online_learning_platform.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-learning/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategories(){
        List<AllCategoriesDto> categories = categoryService.allCategories();
        if(categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Data");
        }

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryCourses(@PathVariable Integer categoryId){
        try {
            CategoryDto category = categoryService.findById(categoryId);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDto categoryDto){
        try {
            CategoryWithoutCoursesDto category = categoryService.create(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId
            , @Valid @RequestBody CategoryDto categoryDto){

        try {
            CategoryDto category = categoryService.update(categoryId, categoryDto);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId){
        try {
            String message = categoryService.delete(categoryId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
