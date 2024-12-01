package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.category.request.CategoryRequestDto;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryResponseDto;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.model.Category;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CategoryMapper {

    public CategoryResponseDto toResponseDto(Category category, Set<AllCoursesDto> allCourses){

        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .courses(allCourses)
                .build();
    }

    public CategoryDtoWithoutCourses toCategoryDtoWithoutCourses(Category category){

        return CategoryDtoWithoutCourses.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .build();
    }

    public Category toEntity(CategoryRequestDto categoryRequestDto){
        return Category.builder()
                .categoryName(categoryRequestDto.getName())
                .build();
    }
}
