package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.category.AllCategoriesDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryDto;
import com.onlinelearning.online_learning_platform.dto.category.CategoryWithoutCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.model.Category;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category, Set<AllCoursesDto> allCourses){

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .courses(allCourses)
                .build();
    }

    public CategoryWithoutCoursesDto toCategoryWithoutCoursesDto(Category category){

        return CategoryWithoutCoursesDto.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .build();
    }

    public AllCategoriesDto toAllDto(Category category){

        return AllCategoriesDto.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .build();
    }

    public Category toEntity(CategoryDto categoryDto){
        return Category.builder()
                .categoryName(categoryDto.getName())
                .build();
    }
}
