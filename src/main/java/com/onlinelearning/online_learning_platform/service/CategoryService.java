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
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
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
                           , CourseMapper courseMapper, CourseRepository courseRepository){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.courseMapper = courseMapper;
    }

    public List<CategoryDtoWithoutCourses> allCategories() {

        List<Category> categories = categoryRepository.findAll();

        List<CategoryDtoWithoutCourses> allCategories = categories.stream()
                .map(category -> categoryMapper.toCategoryDtoWithoutCourses(category)).toList();

        return allCategories;
    }

    public CategoryResponseDto findById(Integer categoryId) {

        Category category = checkCategoryExistById(categoryId);
        Set<AllCoursesDto> allCourses = category.getCourses().stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());

        return categoryMapper.toResponseDto(category, allCourses);
    }

    @Transactional
    public CategoryDtoWithoutCourses create(CategoryRequestDto categoryRequestDto){

        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryRequestDto.getName());
        if(optionalCategory.isPresent()){
            throw new CategoryException("Category already exist");
        }

        Category category = categoryMapper.toEntity(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDtoWithoutCourses(savedCategory);
    }

    @Transactional
    public CategoryResponseDto update(Integer categoryId, CategoryRequestDto categoryRequestDto) {

        Category category = checkCategoryExistById(categoryId);
        category.setCategoryName(categoryRequestDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        Set<AllCoursesDto> allCourses = updatedCategory.getCourses().stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());

        return categoryMapper.toResponseDto(updatedCategory, allCourses);
    }

    @Transactional
    public String delete(Integer categoryId) {

        Category category = checkCategoryExistById(categoryId);

        Category dummyCategory = categoryRepository.findByCategoryName("Uncategorized")
                .orElseThrow(() -> new CategoryException("Dummy category not found. Please create an 'Uncategorized' category."));

        categoryRepository.updateCategoryForCourse(categoryId, dummyCategory.getId());
        categoryRepository.delete(category);
        return "Category deleted successfully with ID: "+ category.getId();
    }

    public Category checkCategoryExistById(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isEmpty()){
            throw new CategoryException("Category not found");
        }

        return optionalCategory.get();
    }
}
