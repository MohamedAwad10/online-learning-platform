package com.onlinelearning.online_learning_platform.service.category;

import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.exception.CategoryException;
import com.onlinelearning.online_learning_platform.mapper.CategoryMapper;
import com.onlinelearning.online_learning_platform.model.Category;
import com.onlinelearning.online_learning_platform.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryHandlerService {

    private CategoryRepository categoryRepository;

    private CategoryMapper categoryMapper;

    public CategoryHandlerService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category validateCategoryExistById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));
    }

    public void validateCategoryNameUniqueness(String categoryName){
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        if(optionalCategory.isPresent()){
            throw new CategoryException("Category already exist");
        }
    }

    public void handleUncategorizedOnDelete(Integer categoryId){
        Category dummyCategory = categoryRepository.findByCategoryName("Uncategorized")
                .orElseThrow(() ->
                        new CategoryException("Dummy category not found. Please create an 'Uncategorized' category."));
        categoryRepository.updateCategoryForCourse(categoryId, dummyCategory.getId());
    }

    public Category checkCategoryExistByName(String categoryName){
        return categoryRepository
                .findByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryException("Category not found"));
    }

    public CategoryDtoWithoutCourses getCategoryDtoWithoutCourses(Category category){
        return categoryMapper.toCategoryDtoWithoutCourses(category);
    }
}
