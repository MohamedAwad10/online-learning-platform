package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.CourseDTO;
import com.onlinelearning.online_learning_platform.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toCourseEntity(CourseDTO courseDTO){
        return Course.builder()
                .title(courseDTO.getTitle())
                .description(courseDTO.getDescription())
                .tags(courseDTO.getTags())
                .category(courseDTO.getCategory())
                .build();
    }
}
