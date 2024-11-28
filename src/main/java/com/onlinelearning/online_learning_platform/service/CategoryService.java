package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.category.AllCategoriesDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryWithoutCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
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

    public List<AllCategoriesDto> allCategories() {

        List<Category> categories = categoryRepository.findAll();

        List<AllCategoriesDto> allCategories = categories.stream()
                .map(category -> categoryMapper.toAllDto(category)).toList();

        return allCategories;
    }

    @Transactional
    public CategoryWithoutCoursesDto create(CategoryDto categoryDto) throws Exception{

        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryDto.getName());
        if(optionalCategory.isPresent()){
            throw new Exception("Category already exist");
        }

        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryWithoutCoursesDto(savedCategory);
    }

    public CategoryDto findById(Integer categoryId) throws Exception{

        Category category = checkCategoryExist(categoryId);
        Set<AllCoursesDto> allCourses = category.getCourses().stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());

        return categoryMapper.toDto(category, allCourses);
    }

    public CategoryDto update(Integer categoryId, CategoryDto categoryDto) throws Exception{

        Category category = checkCategoryExist(categoryId);
        category.setCategoryName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        Set<AllCoursesDto> allCourses = updatedCategory.getCourses().stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());

        return categoryMapper.toDto(updatedCategory, allCourses);
    }

    @Transactional
    public String delete(Integer categoryId) throws Exception{

        Category category = checkCategoryExist(categoryId);

        Category dummyCategory = categoryRepository.findByCategoryName("Uncategorized")
                .orElseThrow(() -> new RuntimeException("Dummy category not found. Please create an 'Uncategorized' category."));

        categoryRepository.updateCategoryForCourse(categoryId, dummyCategory.getId());
        categoryRepository.delete(category);
        return "Category deleted successfully with ID: "+ category.getId();
    }

    public Category checkCategoryExist(Integer categoryId) throws Exception{
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isEmpty()){
            throw new Exception("Category not found");
        }

        return optionalCategory.get();
    }
}
