package com.onlinelearning.online_learning_platform.dto.category.response;

import lombok.*;

@Setter
@Getter
@Builder
public class CategoryDtoWithoutCourses {

    private Integer id;
    private String name;
}
