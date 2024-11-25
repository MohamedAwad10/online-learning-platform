package com.onlinelearning.online_learning_platform.dto.course;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorCoursesDto {

    private String title;

    private String status;

    private String image;
}
