package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.course.CourseCreationDTO;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.CourseDto;
import com.onlinelearning.online_learning_platform.dto.lesson.LessonDto;
import com.onlinelearning.online_learning_platform.dto.review.ReviewDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.enums.CourseStatus;
import com.onlinelearning.online_learning_platform.mapper.*;
import com.onlinelearning.online_learning_platform.model.Category;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.model.Tag;
import com.onlinelearning.online_learning_platform.repository.CategoryRepository;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import com.onlinelearning.online_learning_platform.repository.InstructorRepository;
import com.onlinelearning.online_learning_platform.repository.TagRepository;
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
    private CourseMapper courseMapper;
    private InstructorRepository instructorRepository;
    private CategoryRepository categoryRepository;
    private UserMapper userMapper;
    private LessonMapper lessonMapper;
    private ReviewMapper reviewMapper;
    private TagMapper tagMapper;
    private TagRepository tagRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, UserMapper userMapper
            , InstructorRepository instructorRepository, CategoryRepository categoryRepository
            , LessonMapper lessonMapper, ReviewMapper reviewMapper, TagMapper tagMapper
            , TagRepository tagRepository){
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.instructorRepository = instructorRepository;
        this.categoryRepository = categoryRepository;
        this.userMapper = userMapper;
        this.lessonMapper = lessonMapper;
        this.reviewMapper = reviewMapper;
        this.tagMapper = tagMapper;
        this.tagRepository = tagRepository;
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

        Optional<Category> optionalCategory = categoryRepository
                .findByCategoryName(courseCreationDTO.getCategory());
        Category category = optionalCategory.orElseThrow(() -> new RuntimeException("Category not found"));

        Set<Tag> tags = courseCreationDTO.getTags().stream()
                .map(tagDto -> tagRepository
                        .findByTagName(tagDto.getTagName())
                        .orElseGet(() -> tagMapper.toEntity(tagDto))).collect(Collectors.toSet());

        Course course = courseMapper.toCourseEntity(courseCreationDTO, category, tags);
        course.setStatus(CourseStatus.DRAFT.toString());
        course.setInstructor(instructor);


        courseRepository.save(course);
        return courseCreationDTO;
    }

    @Transactional
    public CourseCreationDTO update(Integer courseId, CourseCreationDTO courseCreationDTO) throws Exception{

        Course course = checkCourseExist(courseId);

        Optional<Category> optionalCategory = categoryRepository
                .findByCategoryName(courseCreationDTO.getCategory());
        Category category = optionalCategory.orElseThrow();

        course.setTitle(courseCreationDTO.getTitle());
        course.setDescription(courseCreationDTO.getDescription());
        course.setCategory(category);

        Set<Tag> tags = courseCreationDTO.getTags().stream()
                .map(tagDto -> tagRepository
                        .findByTagName(tagDto.getTagName())
                        .orElseGet(() -> tagMapper.toEntity(tagDto))).collect(Collectors.toSet());
        course.setTags(tags);

        courseRepository.save(course);
        return courseCreationDTO;
    }

    public String delete(int courseId) throws Exception{

        Course course = checkCourseExist(courseId);
        courseRepository.delete(course);

        return "Course deleted successfully";
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

        Course course = checkCourseExist(courseId);

        course.setStatus(CourseStatus.PENDING.toString());
        courseRepository.save(course);

        return "Course submitted for review successfully";
    }

    public CourseDto findById(Integer courseId) throws Exception{

        Course course = checkCourseExist(courseId);

        CourseInstructorDto courseInstructorDto = userMapper.toCourseInstructorDto(course.getInstructor());

        List<LessonDto> lessons = course.getLessons().stream()
                .map(lesson -> lessonMapper.toDto(lesson)).toList();

        Set<ReviewDto> reviews = course.getReviews().stream()
                .map(review -> reviewMapper.toReviewDto(review)).collect(Collectors.toSet());

        return courseMapper.toFullCourseDto(course, courseInstructorDto, lessons, reviews);
    }

    public Course checkCourseExist(Integer courseId) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found");
        }

        return optionalCourse.get();
    }
}