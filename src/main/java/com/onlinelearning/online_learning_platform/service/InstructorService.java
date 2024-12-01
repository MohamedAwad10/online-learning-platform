package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.response.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.response.InstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.UserContactDto;
import com.onlinelearning.online_learning_platform.exception.UserNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private InstructorRepository instructorRepository;
    private UserMapper userMapper;
    private CourseMapper courseMapper;

    public InstructorService(InstructorRepository instructorRepository, UserMapper userMapper
                            , CourseMapper courseMapper) {
        this.instructorRepository = instructorRepository;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
    }

    public InstructorDto getById(Integer instructorId) {

        Instructor instructor = checkInstructorExist(instructorId);

        Set<AllCoursesDto> allCourses = instructor.getCourses().stream()
                .map(course -> courseMapper.toAllCoursesDto(course)).collect(Collectors.toSet());

        Set<UserContactDto> contactDtos = instructor.getContacts().stream()
                .map(contact -> userMapper.toUserContactDto(contact)).collect(Collectors.toSet());

        return userMapper.toInstructorDto(instructor, allCourses, contactDtos);
    }

    public List<InstructorCoursesDto> getAllCourses(Integer instructorId) {

        Instructor instructor = checkInstructorExist(instructorId);

        Set<Course> courses
                = instructorRepository.findAllCoursesById(instructorId);

        return courses.stream().map(course -> courseMapper.toInstructorCoursesDto(course)).toList();
    }

    public Set<InstructorCoursesDto> searchCourses(Integer instructorId, String keyword) {

        checkInstructorExist(instructorId);
        Set<Course> searchedCourses = instructorRepository.searchInstructorCourses(instructorId, keyword);

        Set<InstructorCoursesDto> courses = searchedCourses.stream()
                .map(course -> courseMapper.toInstructorCoursesDto(course)).collect(Collectors.toSet());

        return courses;
    }

    public Instructor checkInstructorExist(int instructorId) {
        Optional<Instructor> optionalInstructor = instructorRepository.findById(instructorId);
        if (optionalInstructor.isEmpty()) {
            throw new UserNotFoundException("Instructor not found");
        }

       return optionalInstructor.get();
    }
}
