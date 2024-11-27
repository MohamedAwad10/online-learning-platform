package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.course.CourseCreationDTO;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.FullCourseDto;
import com.onlinelearning.online_learning_platform.dto.course.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.model.Category;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public Course toCourseEntity(CourseCreationDTO courseCreationDTO, Category category, Set<Tag> tags){

        return Course.builder()
                .title(courseCreationDTO.getTitle())
                .description(courseCreationDTO.getDescription())
                .tags(tags)
                .category(category)
                .build();
    }

    public AllCoursesDto toAllCoursesDto(Course course){

        return AllCoursesDto.builder()
                .title(course.getTitle())
                .category(course.getCategory().getCategoryName())
                .instructorName(course.getInstructor().getFirstName()+" "+course.getInstructor().getLastName())
                .reviews(course.getReviews().size())
                .lessons(course.getLessons().size())
                .students(course.getEnrollments().size())
                .image(course.getImage())
                .build();
    }

    public FullCourseDto toFullCourseDto(Course course, CourseInstructorDto courseInstructorDto
            , List<LessonDto> lessonsDto, Set<ReviewDto> reviews){

        return FullCourseDto.builder()
                .title(course.getTitle())
                .description(course.getDescription())
                .instructor(courseInstructorDto)
                .lessons(lessonsDto)
                .createdAt(course.getCreatedAt().toString())
                .updatedAt(course.getUpdatedAt().toString())
                .image(course.getImage())
                .tags(course.getTags().stream().map(Tag::getTagName).collect(Collectors.toSet()))
                .category(course.getCategory().getCategoryName())
                .numberOfEnrollments(course.getEnrollments().size())
                .reviews(reviews)
                .build();
    }

    public InstructorCoursesDto toInstructorCoursesDto(Course course){

        return InstructorCoursesDto.builder()
                .title(course.getTitle())
                .status(course.getStatus())
                .image(course.getImage())
                .build();
    }
}
