package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.course.CourseCreationDTO;
import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.CourseDto;
import com.onlinelearning.online_learning_platform.enums.CourseStatus;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import com.onlinelearning.online_learning_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper
            , UserRepository userRepository){
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
    }

    public String insert(Integer instructorId, CourseCreationDTO courseCreationDTO) throws Exception{

//        Optional<Instructor> optionalInstructor = userRepository.findById(instructorId);

        Optional<Course> optionalCourse = courseRepository.findByTitle(courseCreationDTO.getTitle());
        if(optionalCourse.isPresent()){
            throw new Exception("Course already exists with this title");
        }

        Course course = courseMapper.toCourseEntity(courseCreationDTO);
        course.setStatus(CourseStatus.DRAFT.toString());

        courseRepository.save(course);
        return "Course created Successfully";
    }

    public String update(Integer courseId, CourseCreationDTO courseCreationDTO) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found.");
        }

        Course course = optionalCourse.get();
        course.setTitle(courseCreationDTO.getTitle());
        course.setDescription(courseCreationDTO.getDescription());
        course.setCategory(courseCreationDTO.getCategory());
        course.setTags(courseCreationDTO.getTags());

        courseRepository.save(course);
        return "Course updated successfully";
    }

    public boolean delete(int courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            return false;
//            throw new Exception ("Course not found");/
        }

        Course course = optionalCourse.get();
        courseRepository.delete(course);
//        return "Course deleted successfully";
        return true;
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

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found");
        }
        Course course = optionalCourse.get();
        course.setStatus(CourseStatus.PENDING.toString());
//        courseRepository.save(course);

        return "Course submitted for review successfully";
    }

    public CourseDto findById(Integer courseId) throws Exception{

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new Exception("Course not found.");
        }

        Course course = optionalCourse.get();

        return courseMapper.toFullCourseDto(course);
    }
}