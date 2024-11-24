package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.CourseInstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.InstructorDto;
import com.onlinelearning.online_learning_platform.dto.user.UserDto;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public AllUsersDto toUserResponseDto(Users user){
        return AllUsersDto.builder()
                .fullName(user.getFirstName()+" "+user.getLastName())
                .email(user.getEmail())
                .image(user.getProfileImage())
                .joinedDate(user.getJoinedDate().toString())
                .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                .build();
    }

    public Student toStudentEntity(UserDto userDto){

        return Student.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .contacts(userDto.getContacts())
                .profileImage(userDto.getImage())
                .birthDate(LocalDate.parse(userDto.getBirthDate()))
                .build();
    }

    public Instructor toInstructorEntity(Users user){

        return Instructor.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .joinedDate(user.getJoinedDate())
                .roles(user.getRoles())
                .contacts(user.getContacts())
                .profileImage(user.getProfileImage())
                .build();
    }

    public CourseInstructorDto toCourseInstructorDto(Instructor instructor){
        return CourseInstructorDto.builder()
                .fullName(instructor.getFirstName()+" "+instructor.getLastName())
                .bio(instructor.getBio())
                .yearsOfExperience(instructor.getYearsOfExperience())
                .contacts(instructor.getContacts())
                .image(instructor.getProfileImage())
                .build();
    }

    public InstructorDto toInstructorDto(Instructor instructor, Set<AllCoursesDto> allCourses){

        long totalEnrolledStudents = instructor.getCourses()
                .stream()
                .mapToLong(course -> course.getEnrollments().size())
                .sum();

        long totalReviews = instructor.getCourses()
                .stream()
                .mapToLong(course -> course.getReviews().size())
                .sum();

        return InstructorDto.builder()
                .fullName(instructor.getFirstName()+" "+instructor.getLastName())
                .bio(instructor.getBio())
                .yearsOfExperience(instructor.getYearsOfExperience())
                .courses(allCourses)
                .totalStudents(totalEnrolledStudents)
                .reviews(totalReviews)
                .contacts(instructor.getContacts())
                .image(instructor.getProfileImage())
                .build();
    }
}
