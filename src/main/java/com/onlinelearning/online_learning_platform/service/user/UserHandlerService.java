package com.onlinelearning.online_learning_platform.service.user;

import com.onlinelearning.online_learning_platform.dto.course.response.InstructorCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.UserContactDto;
import com.onlinelearning.online_learning_platform.dto.user.response.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.exception.EmailAlreadyInUseException;
import com.onlinelearning.online_learning_platform.exception.RoleAlreadyExistException;
import com.onlinelearning.online_learning_platform.exception.RoleNotFoundException;
import com.onlinelearning.online_learning_platform.exception.UserNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.CourseMapper;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import com.onlinelearning.online_learning_platform.repository.InstructorRepository;
import com.onlinelearning.online_learning_platform.repository.RoleRepository;
import com.onlinelearning.online_learning_platform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserHandlerService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private UserMapper userMapper;

    private CourseMapper courseMapper;

    private InstructorRepository instructorRepository;

    public UserHandlerService(UserRepository userRepository, RoleRepository roleRepository
            , UserMapper userMapper, CourseMapper courseMapper, InstructorRepository instructorRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
        this.instructorRepository = instructorRepository;
    }

    public Users checkUserExist(Integer userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        return optionalUser.get();
    }

    public void checkEmailUniqueness(String email){
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new EmailAlreadyInUseException("This email is already in use");
        }
    }

    public Role checkRole(Users user, String roleName){
        if(user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(roleName))){
            throw new RoleAlreadyExistException("User already has "+ roleName +" role");
        }

        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName +" role not found"));
    }

    public Set<UserContactDto> getAllUserContactsDto(Set<UserContacts> contactsSet){
        return contactsSet.stream()
                .map(contact -> userMapper.toUserContactDto(contact)).collect(Collectors.toSet());
    }

    public Instructor checkInstructorExist(int instructorId) {
        Optional<Instructor> optionalInstructor = instructorRepository.findById(instructorId);
        if (optionalInstructor.isEmpty()) {
            throw new UserNotFoundException("Instructor not found");
        }
        return optionalInstructor.get();
    }

    public CourseInstructorDto getCourseInstructorDto(Instructor instructor){
        Set<UserContactDto> contactsDto = getAllUserContactsDto(instructor.getContacts());
        return userMapper.toCourseInstructorDto(instructor, contactsDto);
    }

    public Stream<InstructorCoursesDto> allInstructorCoursesDto(List<Course> courses){
        return courses.stream()
                .map(course -> courseMapper.toInstructorCoursesDto(course));
    }
}
