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
import com.onlinelearning.online_learning_platform.service.category.CategoryHandlerService;
import com.onlinelearning.online_learning_platform.service.lesson.LessonHandlerService;
import com.onlinelearning.online_learning_platform.service.lesson.LessonService;
import com.onlinelearning.online_learning_platform.service.review.ReviewHandlerService;
import com.onlinelearning.online_learning_platform.service.review.ReviewService;
import com.onlinelearning.online_learning_platform.service.tag.TagHandlerService;
import com.onlinelearning.online_learning_platform.service.tag.TagService;
import com.onlinelearning.online_learning_platform.service.user.StudentValidator;
import com.onlinelearning.online_learning_platform.service.user.UserHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private CourseHandlerService courseHandlerService;
    private StudentValidator studentValidator;
    private CategoryHandlerService categoryHandlerService;
    private UserHandlerService userHandlerService;
    private LessonHandlerService lessonHandlerService;
    private ReviewHandlerService reviewHandlerService;
    private TagHandlerService tagHandlerService;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper
            , CourseHandlerService courseHandlerService, StudentValidator studentValidator
            , CategoryHandlerService categoryHandlerService, UserHandlerService userHandlerService
            , LessonHandlerService lessonHandlerService, ReviewHandlerService reviewHandlerService
            , TagHandlerService tagHandlerService){
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseHandlerService = courseHandlerService;
        this.studentValidator = studentValidator;
        this.categoryHandlerService = categoryHandlerService;
        this.userHandlerService = userHandlerService;
        this.lessonHandlerService = lessonHandlerService;
        this.reviewHandlerService = reviewHandlerService;
        this.tagHandlerService = tagHandlerService;
    }

    @Transactional
    public CourseResponseDto insert(Integer instructorId, CourseRequestDTO courseRequestDTO) {

        Instructor instructor = userHandlerService.checkInstructorExist(instructorId);
        courseHandlerService.validateCourseTitleUniqueness(courseRequestDTO.getTitle());
        Category category = categoryHandlerService.checkCategoryExistByName(courseRequestDTO.getCategory().getName());

        Set<Tag> tags = tagHandlerService.getTags(courseRequestDTO.getTags());
        Set<TagDto> tagsDto = tagHandlerService.toTagsDto(tags);

        Course course = courseMapper.toCourseEntity(courseRequestDTO, category, tags);
        course.setStatus(CourseStatus.DRAFT.toString());
        course.setInstructor(instructor);

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseCreationResponseDto
                (savedCourse, tagsDto, categoryHandlerService.getCategoryDtoWithoutCourses(category));
    }

    @Transactional
    public CourseResponseDto update(Integer courseId, CourseRequestDTO courseRequestDTO) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        Category category = categoryHandlerService.checkCategoryExistByName(courseRequestDTO.getCategory().getName());

        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setCategory(category);
        course.setImage(courseRequestDTO.getImage());

        Set<Tag> tags = tagHandlerService.getTags(courseRequestDTO.getTags());
        course.setTags(tags);

        Course updatedCourse = courseRepository.save(course);

        Set<TagDto> tagsDto = tagHandlerService.toTagsDto(tags);
        return courseMapper.toCourseCreationResponseDto
                (updatedCourse, tagsDto, categoryHandlerService.getCategoryDtoWithoutCourses(category));
    }

    @Transactional
    public String delete(int courseId) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        courseRepository.delete(course);
        return "Course deleted successfully with ID: " + course.getId();
    }

    public FullCourseDto findById(Integer courseId) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        CourseInstructorDto courseInstructorDto = userHandlerService.getCourseInstructorDto(course.getInstructor());

        List<LessonDto> lessons = lessonHandlerService.getLessonsDto(course.getLessons());
        Set<ReviewDto> reviews = reviewHandlerService.getReviewsDto(course.getReviews());
        CategoryDtoWithoutCourses categoryDto = categoryHandlerService.getCategoryDtoWithoutCourses(course.getCategory());

        return courseMapper.toFullCourseDto(course, courseInstructorDto, lessons, reviews, categoryDto);
    }

    public List<AllCoursesDto> findAllEnrolledCourses(Integer studentId) {

        Student student = studentValidator.checkStudentExist(studentId);
        List<Course> courses = student.getEnrollments().stream().map(Enrollment::getCourse).toList();
        return courses.stream().map(course -> courseMapper.toAllCoursesDto(course)).toList();
    }

    public List<AllCoursesDto> findAllPending() {

        List<Course> courses = courseRepository.findAllPending();
        return courseHandlerService.getAllCoursesDto(courses);
    }

    public List<AllCoursesDto> findAllApproved() {

        List<Course> courses = courseRepository.findAllApproved();
        return courseHandlerService.getAllCoursesDto(courses);
    }

    @Transactional
    public String submitCourseForReview(Integer courseId) {
        Course course = courseHandlerService.checkCourseExist(courseId);
        course.setStatus(CourseStatus.PENDING.toString());
        courseRepository.save(course);

        return "Course submitted for review successfully";
    }

    @Transactional
    public String approveCourse(Integer courseId) {
        Course course = courseHandlerService.checkPendingCourseExist(courseId);
        if(course.getStatus().equals(CourseStatus.APPROVED.toString())){
            throw new CourseException("Course has been approved before");
        }
        course.setStatus(CourseStatus.APPROVED.toString());
        courseRepository.save(course);

        return "Course approved successfully";
    }

    @Transactional
    public String rejectCourse(Integer courseId) {
        Course course = courseHandlerService.checkPendingCourseExist(courseId);
        if(course.getStatus().equals(CourseStatus.REJECTED.toString())){
            throw new CourseException("Course has been rejected before");
        }
        course.setStatus(CourseStatus.REJECTED.toString());
        courseRepository.save(course);

        return "The course has been rejected.";
    }

    public List<AllCoursesDto> searchCourses(String keyword) {
        List<Course> courses = courseRepository.searchApprovedCourses(keyword);
        return courseHandlerService.getAllCoursesDto(courses);
    }
}