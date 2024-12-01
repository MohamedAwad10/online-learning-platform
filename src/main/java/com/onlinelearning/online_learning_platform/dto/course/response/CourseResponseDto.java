package com.onlinelearning.online_learning_platform.dto.course.response;

import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class CourseResponseDto {

    private Integer id;

    private String title;

    private String description;

    private Set<TagDto> tags;

    private CategoryDtoWithoutCourses category;

    private String image;

    private String createdAt;
}
