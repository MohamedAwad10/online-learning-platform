package com.onlinelearning.online_learning_platform.service.category;

import com.onlinelearning.online_learning_platform.dto.category.request.CategoryRequestDto;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryResponseDto;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.mapper.CategoryMapper;
import com.onlinelearning.online_learning_platform.model.Category;
import com.onlinelearning.online_learning_platform.repository.CategoryRepository;
import com.onlinelearning.online_learning_platform.service.course.CourseHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    private CategoryMapper categoryMapper;

    private CategoryHandlerService categoryHandlerService;

    private CourseHandlerService courseHandlerService;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper
            , CategoryHandlerService categoryHandlerService, CourseHandlerService courseHandlerService){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categoryHandlerService = categoryHandlerService;
        this.courseHandlerService = courseHandlerService;
    }

    public List<CategoryDtoWithoutCourses> allCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toCategoryDtoWithoutCourses)
                .toList();
    }

    public CategoryResponseDto findById(Integer categoryId) {
        Category category = categoryHandlerService.validateCategoryExistById(categoryId);
        Set<AllCoursesDto> allCourses = courseHandlerService.getAllCoursesDto(category.getCourses());
        return categoryMapper.toResponseDto(category, allCourses);
    }

    @Transactional
    public CategoryDtoWithoutCourses create(CategoryRequestDto categoryRequestDto){
        categoryHandlerService.validateCategoryNameUniqueness(categoryRequestDto.getName());
        Category category = categoryMapper.toEntity(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDtoWithoutCourses(savedCategory);
    }

    @Transactional
    public CategoryResponseDto update(Integer categoryId, CategoryRequestDto categoryRequestDto) {
        Category category = categoryHandlerService.validateCategoryExistById(categoryId);
        category.setCategoryName(categoryRequestDto.getName());

        Category updatedCategory = categoryRepository.save(category);
        Set<AllCoursesDto> allCourses = courseHandlerService.getAllCoursesDto(updatedCategory.getCourses());

        return categoryMapper.toResponseDto(updatedCategory, allCourses);
    }

    @Transactional
    public String delete(Integer categoryId) {
        Category category = categoryHandlerService.validateCategoryExistById(categoryId);
        categoryHandlerService.handleUncategorizedOnDelete(categoryId);
        categoryRepository.delete(category);
        return "Category deleted successfully with ID: "+ category.getId();
    }
}
