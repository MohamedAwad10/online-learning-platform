package com.onlinelearning.online_learning_platform.dto.category.response;

import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class CategoryResponseDto {

    private Integer id;

    private String name;

    private Set<AllCoursesDto> courses;
}
