package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.category.request.CategoryRequestDto;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryResponseDto;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.exception.CategoryException;
import com.onlinelearning.online_learning_platform.mapper.CategoryMapper;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.model.Category;
import com.onlinelearning.online_learning_platform.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    private CategoryMapper categoryMapper;

    private CourseMapper courseMapper;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper
                           , CourseMapper courseMapper){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.courseMapper = courseMapper;
    }

    public List<CategoryDtoWithoutCourses> allCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::toCategoryDtoWithoutCourses)
                .toList();

    }

    public CategoryResponseDto findById(Integer categoryId) {

        Category category = validateCategoryExistById(categoryId);
        Set<AllCoursesDto> allCourses = getAllCategoryCoursesDto(category);
        return categoryMapper.toResponseDto(category, allCourses);
    }

    @Transactional
    public CategoryDtoWithoutCourses create(CategoryRequestDto categoryRequestDto){

        validateCategoryNameUniqueness(categoryRequestDto.getName());
        Category category = categoryMapper.toEntity(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDtoWithoutCourses(savedCategory);
    }

    @Transactional
    public CategoryResponseDto update(Integer categoryId, CategoryRequestDto categoryRequestDto) {

        Category category = validateCategoryExistById(categoryId);
        category.setCategoryName(categoryRequestDto.getName());

        Category updatedCategory = categoryRepository.save(category);
        Set<AllCoursesDto> allCourses = getAllCategoryCoursesDto(updatedCategory);

        return categoryMapper.toResponseDto(updatedCategory, allCourses);
    }

    @Transactional
    public String delete(Integer categoryId) {

        Category category = validateCategoryExistById(categoryId);
        handleUncategorizedOnDelete(categoryId);
        categoryRepository.delete(category);
        return "Category deleted successfully with ID: "+ category.getId();
    }

    public Set<AllCoursesDto> getAllCategoryCoursesDto(Category category){
        return category.getCourses().stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());
    }

    private Category validateCategoryExistById(Integer categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));
    }

    private void validateCategoryNameUniqueness(String categoryName){
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        if(optionalCategory.isPresent()){
            throw new CategoryException("Category already exist");
        }
    }

    private void handleUncategorizedOnDelete(Integer categoryId){
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
