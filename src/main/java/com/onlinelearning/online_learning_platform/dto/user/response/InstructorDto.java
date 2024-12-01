package com.onlinelearning.online_learning_platform.dto.user.response;

import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto extends CourseInstructorDto{

    private long totalStudents;

    private long totalReviews;

    private Set<AllCoursesDto> courses;
}
