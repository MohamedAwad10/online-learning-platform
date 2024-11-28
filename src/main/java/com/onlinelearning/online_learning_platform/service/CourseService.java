package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.commons.Commons;
import com.onlinelearning.online_learning_platform.dto.category.CategoryWithoutCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.CourseCreationDTO;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.FullCourseDto;
import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.UserContactDto;
import com.onlinelearning.online_learning_platform.enums.CourseStatus;
import com.onlinelearning.online_learning_platform.mapper.*;
import com.onlinelearning.online_learning_platform.model.*;
import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import com.onlinelearning.online_learning_platform.model.enrollment.EnrollmentID;
import com.onlinelearning.online_learning_platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
    public CourseCreationDTO insert(Integer instructorId, CourseCreationDTO courseCreationDTO) throws Exception{

        Optional<Instructor> optionalInstructor = instructorRepository.findById(instructorId);
        if(optionalInstructor.isEmpty()){
            throw new Exception("Instructor not found");
        }
        Instructor instructor = optionalInstructor.get();

        Optional<Course> optionalCourse = courseRepository.findByTitle(courseCreationDTO.getTitle());
        if(optionalCourse.isPresent()){
            throw new Exception("Course already exists with this title");
        }

        Category category = categoryRepository
                .findByCategoryName(courseCreationDTO.getCategory().getName())
                .orElseThrow(() -> new RuntimeException("Category not found"));;

        Set<Tag> tags = courseCreationDTO.getTags().stream()
                .map(tagDto -> tagRepository
                        .findByTagName(tagDto.getTagName())
                        .orElseGet(() -> tagMapper.toEntity(tagDto))).collect(Collectors.toSet());

        Course course = courseMapper.toCourseEntity(courseCreationDTO, category, tags);
        course.setStatus(CourseStatus.DRAFT.toString());
        course.setInstructor(instructor);

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toCourseCreationDto
                (savedCourse, courseCreationDTO.getTags(), categoryMapper.toCategoryWithoutCoursesDto(category));
    }

    @Transactional
    public CourseCreationDTO update(Integer courseId, CourseCreationDTO courseCreationDTO) throws Exception{

        Course course = common.checkCourseExist(courseId);

        Optional<Category> optionalCategory = categoryRepository
                .findByCategoryName(courseCreationDTO.getCategory().getName());
        Category category = optionalCategory.orElseThrow(() -> new RuntimeException("Category not found"));

        course.setTitle(courseCreationDTO.getTitle());
        course.setDescription(courseCreationDTO.getDescription());
        course.setCategory(category);

        Set<Tag> tags = courseCreationDTO.getTags().stream()
                .map(tagDto -> tagRepository
                        .findByTagName(tagDto.getTagName())
                        .orElseGet(() -> tagMapper.toEntity(tagDto))).collect(Collectors.toSet());
        course.setTags(tags);

        Course updatedCourse = courseRepository.save(course);

        return courseMapper.toCourseCreationDto
                (updatedCourse, courseCreationDTO.getTags(), categoryMapper.toCategoryWithoutCoursesDto(category));
    }

    public String delete(int courseId) throws Exception{

        Course course = common.checkCourseExist(courseId);
        courseRepository.delete(course);

        return "Course deleted successfully with ID: " + course.getId();
    }

    @Transactional
    public String enrollIn(Integer courseId, Integer studentId) throws Exception{

        Course course = checkApprovedCourseExist(courseId);
        Student student = common.checkStudentExist(studentId);

        EnrollmentID enrollmentID = new EnrollmentID(student, course);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentID);
        if(optionalEnrollment.isPresent()){
            throw new RuntimeException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        enrollmentRepository.save(enrollment);
        return "Enrolled in successfully in Course with ID: "+ course.getId();
    }

    public List<AllCoursesDto> findAllEnrolledCourses(Integer studentId) throws Exception{

        Student student = common.checkStudentExist(studentId);
        List<Course> courses = student.getEnrollments().stream().map(Enrollment::getCourse).toList();

        return courses.stream().map(course -> courseMapper.toAllCoursesDto(course)).toList();
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

    public String submitCourse(Integer courseId) throws Exception{

        Course course = common.checkCourseExist(courseId);

        course.setStatus(CourseStatus.PENDING.toString());
        courseRepository.save(course);

        return "Course submitted for review successfully";
    }

    public FullCourseDto findById(Integer courseId) throws Exception{

        Course course = common.checkCourseExist(courseId);

        Set<UserContactDto> contactDtos = course.getInstructor().getContacts().stream()
                .map(contact -> userMapper.toUserContactDto(contact)).collect(Collectors.toSet());

        CourseInstructorDto courseInstructorDto = userMapper
                .toCourseInstructorDto(course.getInstructor(), contactDtos);

        List<LessonDto> lessons = course.getLessons().stream()
                .map(lesson -> lessonMapper.toDto(lesson)).toList();

        Set<ReviewDto> reviews = course.getReviews().stream()
                .map(review -> reviewMapper.toDto(review)).collect(Collectors.toSet());

        CategoryWithoutCoursesDto categoryDto = categoryMapper.toCategoryWithoutCoursesDto(course.getCategory());

        return courseMapper.toFullCourseDto(course, courseInstructorDto, lessons, reviews, categoryDto);
    }

    public Course checkApprovedCourseExist(Integer courseId) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty() || !optionalCourse.get().getStatus().equals("APPROVED")){
            throw new Exception("Course not found");
        }

        return optionalCourse.get();
    }
}