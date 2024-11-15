package com.onlinelearning.online_learning_platform.dto.course;

import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.model.lesson.Lesson;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class CourseDto {

    private String title;

    private String description;

    private String createdAt;

    private String updatedAt;

    private String image;

    private Set<String> tags;

    private Instructor instructor;

    private String category;

    private Set<Lesson> lessons;

    private Set<ReviewDto> reviews;

    private int numberOfEnrollments;
}
