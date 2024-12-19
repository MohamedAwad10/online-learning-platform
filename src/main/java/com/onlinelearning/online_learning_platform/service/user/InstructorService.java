package com.onlinelearning.online_learning_platform.service.user;

import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.course.response.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.response.InstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.UserContactDto;
import com.onlinelearning.online_learning_platform.dto.user.response.SearchedInstructorDto;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.repository.InstructorRepository;
import com.onlinelearning.online_learning_platform.service.course.CourseHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private InstructorRepository instructorRepository;
    private UserMapper userMapper;
    private CourseHandlerService courseHandlerService;
    private UserHandlerService userHandlerService;

    public InstructorService(InstructorRepository instructorRepository, UserMapper userMapper
            , CourseHandlerService courseHandlerService, UserHandlerService userHandlerService) {
        this.instructorRepository = instructorRepository;
        this.userMapper = userMapper;
        this.courseHandlerService = courseHandlerService;
        this.userHandlerService = userHandlerService;
    }

    public InstructorDto getById(Integer instructorId) {
        Instructor instructor = userHandlerService.checkInstructorExist(instructorId);
        Set<AllCoursesDto> allCourses = courseHandlerService.getAllCoursesDto(instructor.getCourses());
        Set<UserContactDto> contactsDto = userHandlerService.getAllUserContactsDto(instructor.getContacts());
        return userMapper.toInstructorDto(instructor, allCourses, contactsDto);
    }

    public List<InstructorCoursesDto> getAllCourses(Integer instructorId) {
        userHandlerService.checkInstructorExist(instructorId);
        List<Course> courses
                = instructorRepository.findAllCoursesById(instructorId);
        return userHandlerService.allInstructorCoursesDto(courses).toList();
    }

    public Set<InstructorCoursesDto> searchCourses(Integer instructorId, String keyword) {
        userHandlerService.checkInstructorExist(instructorId);
        List<Course> searchedCourses = instructorRepository.searchInstructorCourses(instructorId, keyword);;
        return userHandlerService.allInstructorCoursesDto(searchedCourses).collect(Collectors.toSet());
    }

    public List<SearchedInstructorDto> searchInstructor(String keyword) {
        List<Instructor> instructorList = instructorRepository.searchInstructors(keyword);
        return instructorList.stream()
                .map(instructor -> userMapper.toSearchedInstructorDto(instructor)).toList();
    }
}
