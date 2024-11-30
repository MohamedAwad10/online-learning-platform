package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.category.AllCategoriesDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryDtoWithoutCourses;
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

        CategoryDto category = categoryService.findById(categoryId);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDto categoryDto){

        CategoryDtoWithoutCourses category = categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId
            , @Valid @RequestBody CategoryDto categoryDto){

        CategoryDto category = categoryService.update(categoryId, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId){

        String message = categoryService.delete(categoryId);
        return ResponseEntity.ok(message);
    }
}
