package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.commons.Commons;
import com.onlinelearning.online_learning_platform.dto.category.response.CategoryDtoWithoutCourses;
import com.onlinelearning.online_learning_platform.dto.course.request.CourseRequestDTO;
import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.response.CourseResponseDto;
import com.onlinelearning.online_learning_platform.dto.course.response.FullCourseDto;
import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import com.onlinelearning.online_learning_platform.dto.user.response.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.UserContactDto;
import com.onlinelearning.online_learning_platform.enums.CourseStatus;
import com.onlinelearning.online_learning_platform.exception.CategoryException;
import com.onlinelearning.online_learning_platform.exception.CourseException;
import com.onlinelearning.online_learning_platform.exception.EnrollmentException;
import com.onlinelearning.online_learning_platform.exception.UserNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.*;
import com.onlinelearning.online_learning_platform.model.*;
import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import com.onlinelearning.online_learning_platform.model.enrollment.EnrollmentID;
import com.onlinelearning.online_learning_platform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private InstructorRepository instructorRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;
    private EnrollmentRepository enrollmentRepository;
    private CourseMapper courseMapper;
    private UserMapper userMapper;
    private LessonMapper lessonMapper;
    private ReviewMapper reviewMapper;
    private TagMapper tagMapper;
    private CategoryMapper categoryMapper;
    private Commons common;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, UserMapper userMapper
            , InstructorRepository instructorRepository, CategoryRepository categoryRepository
            , LessonMapper lessonMapper, ReviewMapper reviewMapper, TagMapper tagMapper, CategoryMapper categoryMapper
            , TagRepository tagRepository, Commons common, EnrollmentRepository enrollmentRepository){
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.instructorRepository = instructorRepository;
        this.categoryRepository = categoryRepository;
        this.userMapper = userMapper;
        this.lessonMapper = lessonMapper;
        this.reviewMapper = reviewMapper;
        this.tagMapper = tagMapper;
        this.tagRepository = tagRepository;
        this.common = common;
        this.enrollmentRepository = enrollmentRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CourseResponseDto insert(Integer instructorId, CourseRequestDTO courseRequestDTO) {

        Optional<Instructor> optionalInstructor = instructorRepository.findById(instructorId);
        if(optionalInstructor.isEmpty()){
            throw new UserNotFoundException("Instructor not found");
        }
        Instructor instructor = optionalInstructor.get();

        Optional<Course> optionalCourse = courseRepository.findByTitle(courseRequestDTO.getTitle());
        if(optionalCourse.isPresent()){
            throw new CourseException("Course already exists with this title");
        }

        Category category = categoryRepository
                .findByCategoryName(courseRequestDTO.getCategory().getName())
                .orElseThrow(() -> new CategoryException("Category not found"));;

        Set<Tag> tags = courseRequestDTO.getTags().stream()
                .map(tagDto -> tagRepository
                        .findByTagName(tagDto.getTagName())
                        .orElseGet(() -> tagMapper.toEntity(tagDto))).collect(Collectors.toSet());
        Set<TagDto> tagsDto = tags.stream().map(tag -> tagMapper.toDto(tag)).collect(Collectors.toSet());

        Course course = courseMapper.toCourseEntity(courseRequestDTO, category, tags);
        course.setStatus(CourseStatus.DRAFT.toString());
        course.setInstructor(instructor);

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toCourseCreationResponseDto
                (savedCourse, tagsDto, categoryMapper.toCategoryDtoWithoutCourses(category));
    }

    @Transactional
    public CourseResponseDto update(Integer courseId, CourseRequestDTO courseRequestDTO) {

        Course course = common.checkCourseExist(courseId);

        Category category = categoryRepository
                .findByCategoryName(courseRequestDTO.getCategory().getName())
                .orElseThrow(() -> new CategoryException("Category not found"));

        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setCategory(category);
        course.setImage(courseRequestDTO.getImage());

        Set<Tag> tags = courseRequestDTO.getTags().stream()
                .map(tagDto -> tagRepository
                        .findByTagName(tagDto.getTagName())
                        .orElseGet(() -> tagMapper.toEntity(tagDto))).collect(Collectors.toSet());
        course.setTags(tags);

        Course updatedCourse = courseRepository.save(course);

        Set<TagDto> tagsDto = tags.stream().map(tag -> tagMapper.toDto(tag)).collect(Collectors.toSet());

        return courseMapper.toCourseCreationResponseDto
                (updatedCourse, tagsDto, categoryMapper.toCategoryDtoWithoutCourses(category));
    }

    @Transactional
    public String delete(int courseId) {

        Course course = common.checkCourseExist(courseId);
        courseRepository.delete(course);

        return "Course deleted successfully with ID: " + course.getId();
    }

    @Transactional
    public String enrollIn(Integer courseId, Integer studentId) {

        Course course = checkApprovedCourseExist(courseId);
        Student student = common.checkStudentExist(studentId);

        EnrollmentID enrollmentID = new EnrollmentID(student, course);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentID);
        if(optionalEnrollment.isPresent()){
            throw new EnrollmentException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        enrollmentRepository.save(enrollment);
        return "Enrolled in successfully in Course with ID: "+ course.getId();
    }

    public List<AllCoursesDto> findAllEnrolledCourses(Integer studentId) {

        Student student = common.checkStudentExist(studentId);
        List<Course> courses = student.getEnrollments().stream().map(Enrollment::getCourse).toList();

        return courses.stream().map(course -> courseMapper.toAllCoursesDto(course)).toList();
    }

    public FullCourseDto findById(Integer courseId) {

        Course course = common.checkCourseExist(courseId);

        Set<UserContactDto> contactDtos = course.getInstructor().getContacts().stream()
                .map(contact -> userMapper.toUserContactDto(contact)).collect(Collectors.toSet());

        CourseInstructorDto courseInstructorDto = userMapper
                .toCourseInstructorDto(course.getInstructor(), contactDtos);

        List<LessonDto> lessons = course.getLessons().stream()
                .map(lesson -> lessonMapper.toDto(lesson)).toList();

        Set<ReviewDto> reviews = course.getReviews().stream()
                .map(review -> reviewMapper.toDto(review)).collect(Collectors.toSet());

        CategoryDtoWithoutCourses categoryDto = categoryMapper.toCategoryDtoWithoutCourses(course.getCategory());

        return courseMapper.toFullCourseDto(course, courseInstructorDto, lessons, reviews, categoryDto);
    }

    public List<AllCoursesDto> findAllPending() {

        List<Course> courses = courseRepository.findAllPending();
        List<AllCoursesDto> allCourses = courses.stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).toList();

        return allCourses;
    }

    public List<AllCoursesDto> findAllApproved() {

        List<Course> courses = courseRepository.findAllApproved();
        List<AllCoursesDto> allCourses = courses.stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).toList();

        return allCourses;
    }

    @Transactional
    public String submitCourse(Integer courseId) {

        Course course = common.checkCourseExist(courseId);

        course.setStatus(CourseStatus.PENDING.toString());
        courseRepository.save(course);

        return "Course submitted for review successfully";
    }

    @Transactional
    public String approveCourse(Integer courseId) {

        Course course = common.checkCourseExist(courseId);

        if(course.getStatus().equals(CourseStatus.APPROVED.toString())){
            throw new CourseException("Course has been approved before");
        }

        course.setStatus(CourseStatus.APPROVED.toString());
        courseRepository.save(course);

        return "Course approved successfully";
    }

    @Transactional
    public String rejectCourse(Integer courseId) {

        Course course = common.checkCourseExist(courseId);

        course.setStatus(CourseStatus.REJECTED.toString());
        courseRepository.save(course);

        return "The course has been rejected.";
    }

    public List<AllCoursesDto> searchCourses(String keyword) {

        List<Course> courses = courseRepository.searchApprovedCourses(keyword);
        return courses.stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).toList();
    }

    public Course checkApprovedCourseExist(Integer courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty() || !optionalCourse.get().getStatus().equals("APPROVED")){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }
}