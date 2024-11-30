package com.onlinelearning.online_learning_platform.dto.course;

import com.onlinelearning.online_learning_platform.dto.category.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
public class FullCourseDto {

    private Integer id;

    private String title;

    private String description;

    private String createdAt;

    private String updatedAt;

    private String image;

    private Set<String> tags;

    private CourseInstructorDto instructor;

    private CategoryDtoWithoutCourses category;

    private List<LessonDto> lessons;

    private Set<ReviewDto> reviews;

    private int numberOfEnrollments;
}
