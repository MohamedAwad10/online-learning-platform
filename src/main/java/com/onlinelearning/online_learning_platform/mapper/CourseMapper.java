package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.course.CourseCreationDTO;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.CourseDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.model.Category;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CourseMapper {

    private ReviewMapper reviewMapper;

    private LessonMapper lessonMapper;

    @Autowired
    public CourseMapper(ReviewMapper reviewMapper, LessonMapper lessonMapper){
        this.reviewMapper = reviewMapper;
        this.lessonMapper = lessonMapper;
    }

    public Course toCourseEntity(CourseCreationDTO courseCreationDTO, Category category){

        return Course.builder()
                .title(courseCreationDTO.getTitle())
                .description(courseCreationDTO.getDescription())
                .tags(courseCreationDTO.getTags())
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

    public CourseDto toFullCourseDto(Course course, CourseInstructorDto courseInstructorDto){

        return CourseDto.builder()
                .title(course.getTitle())
                .description(course.getDescription())
                .instructor(courseInstructorDto)
                .lessons(course.getLessons().stream()
                        .map(lesson -> lessonMapper.toDto(lesson)).collect(Collectors.toSet()))
                .createdAt(course.getCreatedAt().toString())
                .updatedAt(course.getUpdatedAt().toString())
                .image(course.getImage())
                .tags(course.getTags().stream().map(Tag::getTagName).collect(Collectors.toSet()))
                .category(course.getCategory().getCategoryName())
                .numberOfEnrollments(course.getEnrollments().size())
                .reviews(course.getReviews().stream()
                        .map(review -> reviewMapper.toReviewDto(review)).collect(Collectors.toSet()))
                .build();
    }
}
