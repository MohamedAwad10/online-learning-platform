package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.course.CourseCreationDTO;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.CourseDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.enums.CourseStatus;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Category;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.repository.CategoryRepository;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import com.onlinelearning.online_learning_platform.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private InstructorRepository instructorRepository;
    private CategoryRepository categoryRepository;
    private UserMapper userMapper;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, UserMapper userMapper
            , InstructorRepository instructorRepository, CategoryRepository categoryRepository){
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.instructorRepository = instructorRepository;
        this.categoryRepository = categoryRepository;
        this.userMapper = userMapper;
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
        Category category = optionalCategory.orElseThrow();

        Course course = courseMapper.toCourseEntity(courseCreationDTO, category);
        course.setStatus(CourseStatus.DRAFT.toString());
        course.setInstructor(instructor);


        courseRepository.save(course);
        return courseCreationDTO;
    }

    public CourseCreationDTO update(Integer courseId, CourseCreationDTO courseCreationDTO) throws Exception{

        Course course = checkCourseExist(courseId);

        Optional<Category> optionalCategory = categoryRepository
                .findByCategoryName(courseCreationDTO.getCategory());
        Category category = optionalCategory.orElseThrow();

        course.setTitle(courseCreationDTO.getTitle());
        course.setDescription(courseCreationDTO.getDescription());
        course.setCategory(category);
        course.setTags(courseCreationDTO.getTags());

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

        return courseMapper.toFullCourseDto(course, courseInstructorDto);
    }

    public Course checkCourseExist(Integer courseId) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found");
        }

        return optionalCourse.get();
    }
}