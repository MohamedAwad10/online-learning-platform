package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.category.CategoryDto;
import com.onlinelearning.online_learning_platform.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category){

        return CategoryDto.builder()
                .name(category.getCategoryName())
                .build();
    }

    public Category toEntity(CategoryDto categoryDto){
        return Category.builder()
                .categoryName(categoryDto.getName())
                .build();
    }
}
