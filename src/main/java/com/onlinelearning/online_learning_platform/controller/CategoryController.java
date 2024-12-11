package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.category.response.CategoryResponseDto;
import com.onlinelearning.online_learning_platform.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/online-learning/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryCourses(@PathVariable Integer categoryId){

        CategoryResponseDto category = categoryService.findById(categoryId);
        return ResponseEntity.ok(category);
    }
}
