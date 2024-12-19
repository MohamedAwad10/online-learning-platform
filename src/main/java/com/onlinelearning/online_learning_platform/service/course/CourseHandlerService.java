package com.onlinelearning.online_learning_platform.service.course;

import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.exception.CourseException;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseHandlerService {

    private CourseRepository courseRepository;

    private CourseMapper courseMapper;

    public CourseHandlerService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public Course checkCourseExist(Integer courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty()){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }

    public Course checkApprovedCourseExist(Integer courseId) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty() || !optionalCourse.get().getStatus().equals("APPROVED")){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }

    public Course checkPendingCourseExist(Integer courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(optionalCourse.isEmpty() || !optionalCourse.get().getStatus().equals("PENDING")){
            throw new CourseException("Course not found");
        }

        return optionalCourse.get();
    }

    public void validateCourseTitleUniqueness(String title){
        if(courseRepository.findByTitle(title).isPresent()){
            throw new CourseException("Course already exists with this title");
        }
    }

    public List<AllCoursesDto> getAllCoursesDto(List<Course> courses){
        return courses.stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).toList();
    }

    public Set<AllCoursesDto> getAllCoursesDto(Set<Course> courses){
        return courses.stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());
    }
}
