package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.InstructorDto;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.repository.InstructorRepository;
import com.onlinelearning.online_learning_platform.repository.RoleRepository;
import com.onlinelearning.online_learning_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private InstructorRepository instructorRepository;
    private UserMapper userMapper;
    private CourseMapper courseMapper;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, UserMapper userMapper
                            , CourseMapper courseMapper) {
        this.instructorRepository = instructorRepository;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
    }

    public InstructorDto getById(Integer instructorId) throws Exception{

        Instructor instructor = checkInstructorExist(instructorId);

        Set<AllCoursesDto> allCourses = instructor.getCourses().stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());

        return userMapper.toInstructorDto(instructor, allCourses);
    }

    public List<InstructorCoursesDto> getAllCourses(Integer instructorId) throws Exception{

        Instructor instructor = checkInstructorExist(instructorId);

        Set<Course> courses
                = instructorRepository.findAllCoursesById(instructorId);

        return courses.stream().map(course -> courseMapper.toInstructorCoursesDto(course)).toList();
    }

    public Set<InstructorCoursesDto> searchCourses(Integer instructorId, String keyword) throws Exception{

        checkInstructorExist(instructorId);
        Set<Course> searchedCourses = instructorRepository.searchInstructorCourses(instructorId, keyword);

        Set<InstructorCoursesDto> courses = searchedCourses.stream()
                .map(course -> courseMapper.toInstructorCoursesDto(course)).collect(Collectors.toSet());

        return courses;
    }

    public Instructor checkInstructorExist(int instructorId) throws Exception{
        Optional<Instructor> optionalInstructor = instructorRepository.findById(instructorId);
        if (optionalInstructor.isEmpty()) {
            throw new Exception("Instructor not found");
        }

       return optionalInstructor.get();
    }
}
