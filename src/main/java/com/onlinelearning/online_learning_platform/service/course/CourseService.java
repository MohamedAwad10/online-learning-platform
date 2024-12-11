package com.onlinelearning.online_learning_platform.service.course;

import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.course.request.CourseRequestDTO;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.response.CourseResponseDto;
import com.onlinelearning.online_learning_platform.dto.course.response.FullCourseDto;
import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.dto.user.response.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.enums.CourseStatus;
import com.onlinelearning.online_learning_platform.exception.CourseException;
import com.onlinelearning.online_learning_platform.mapper.*;
import com.onlinelearning.online_learning_platform.model.*;
import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import com.onlinelearning.online_learning_platform.repository.*;
import com.onlinelearning.online_learning_platform.service.*;
import com.onlinelearning.online_learning_platform.service.user.StudentValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private InstructorService instructorService;
    private CategoryService categoryService;
    private TagService tagService;
    private LessonService lessonService;
    private ReviewService reviewService;
    private CourseMapper courseMapper;
    private CourseValidator courseValidator;
    private StudentValidator studentValidator;

    public CourseService(CourseRepository courseRepository, InstructorService instructorService
            , CategoryService categoryService, TagService tagService, LessonService lessonService
            , ReviewService reviewService, CourseMapper courseMapper
            , CourseValidator courseValidator, StudentValidator studentValidator){
        this.courseRepository = courseRepository;
        this.instructorService = instructorService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.lessonService = lessonService;
        this.reviewService = reviewService;
        this.courseMapper = courseMapper;
        this.courseValidator = courseValidator;
        this.studentValidator = studentValidator;
    }

    @Transactional
    public CourseResponseDto insert(Integer instructorId, CourseRequestDTO courseRequestDTO) {

        Instructor instructor = instructorService.checkInstructorExist(instructorId);

        courseValidator.validateCourseTitleUniqueness(courseRequestDTO.getTitle());

        Category category = categoryService.checkCategoryExistByName(courseRequestDTO.getCategory().getName());

        Set<Tag> tags = tagService.getOrCreateTagsDto(courseRequestDTO.getTags());
        Set<TagDto> tagsDto = tagService.toTagsDto(tags);

        Course course = courseMapper.toCourseEntity(courseRequestDTO, category, tags);
        course.setStatus(CourseStatus.DRAFT.toString());
        course.setInstructor(instructor);

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toCourseCreationResponseDto
                (savedCourse, tagsDto, categoryService.getCategoryDtoWithoutCourses(category));
    }

    @Transactional
    public CourseResponseDto update(Integer courseId, CourseRequestDTO courseRequestDTO) {

        Course course = courseValidator.checkCourseExist(courseId);

        Category category = categoryService.checkCategoryExistByName(courseRequestDTO.getCategory().getName());

        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setCategory(category);
        course.setImage(courseRequestDTO.getImage());

        Set<Tag> tags = tagService.getOrCreateTagsDto(courseRequestDTO.getTags());
        course.setTags(tags);

        Course updatedCourse = courseRepository.save(course);

        Set<TagDto> tagsDto = tagService.toTagsDto(tags);

        return courseMapper.toCourseCreationResponseDto
                (updatedCourse, tagsDto, categoryService.getCategoryDtoWithoutCourses(category));
    }

    @Transactional
    public String delete(int courseId) {

        Course course = courseValidator.checkCourseExist(courseId);
        courseRepository.delete(course);

        return "Course deleted successfully with ID: " + course.getId();
    }

    public FullCourseDto findById(Integer courseId) {

        Course course = courseValidator.checkCourseExist(courseId);
        CourseInstructorDto courseInstructorDto = instructorService.getCourseInstructorDto(course.getInstructor());

        List<LessonDto> lessons = lessonService.getLessonsDto(course.getLessons());
        Set<ReviewDto> reviews = reviewService.getReviewsDto(course.getReviews());
        CategoryDtoWithoutCourses categoryDto = categoryService.getCategoryDtoWithoutCourses(course.getCategory());

        return courseMapper.toFullCourseDto(course, courseInstructorDto, lessons, reviews, categoryDto);
    }

    public List<AllCoursesDto> findAllEnrolledCourses(Integer studentId) {

        Student student = studentValidator.checkStudentExist(studentId);
        List<Course> courses = student.getEnrollments().stream().map(Enrollment::getCourse).toList();
        return courses.stream().map(course -> courseMapper.toAllCoursesDto(course)).toList();
    }

    public List<AllCoursesDto> findAllPending() {

        List<Course> courses = courseRepository.findAllPending();
        return getAllCoursesDto(courses);
    }

    public List<AllCoursesDto> findAllApproved() {

        List<Course> courses = courseRepository.findAllApproved();
        return getAllCoursesDto(courses);
    }

    @Transactional
    public String submitCourse(Integer courseId) {

        Course course = courseValidator.checkCourseExist(courseId);

        course.setStatus(CourseStatus.PENDING.toString());
        courseRepository.save(course);

        return "Course submitted for review successfully";
    }

    @Transactional
    public String approveCourse(Integer courseId) {

        Course course = courseValidator.checkPendingCourseExist(courseId);

        if(course.getStatus().equals(CourseStatus.APPROVED.toString())){
            throw new CourseException("Course has been approved before");
        }
        course.setStatus(CourseStatus.APPROVED.toString());
        courseRepository.save(course);

        return "Course approved successfully";
    }

    @Transactional
    public String rejectCourse(Integer courseId) {

        Course course = courseValidator.checkPendingCourseExist(courseId);

        if(course.getStatus().equals(CourseStatus.REJECTED.toString())){
            throw new CourseException("Course has been rejected before");
        }
        course.setStatus(CourseStatus.REJECTED.toString());
        courseRepository.save(course);

        return "The course has been rejected.";
    }

    public List<AllCoursesDto> searchCourses(String keyword) {
        List<Course> courses = courseRepository.searchApprovedCourses(keyword);
        return getAllCoursesDto(courses);
    }

    public List<AllCoursesDto> getAllCoursesDto(List<Course> courses){
        return courses.stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).toList();
    }
}