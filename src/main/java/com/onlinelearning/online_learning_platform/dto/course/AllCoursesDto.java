package com.onlinelearning.online_learning_platform.dto.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AllCoursesDto {

    private String title;

    private String category;

    private String instructorName;

    private int reviews;

    private int lectures;

    private String image;
}
