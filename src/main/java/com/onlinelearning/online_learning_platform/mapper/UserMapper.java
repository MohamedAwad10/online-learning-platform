package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.course.response.AllCoursesDto;
import com.onlinelearning.online_learning_platform.dto.user.*;
import com.onlinelearning.online_learning_platform.dto.user.request.UserRequestDto;
import com.onlinelearning.online_learning_platform.dto.user.response.*;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Student;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class UserMapper {

    public AllUsersDto toAllUsersDto(Users user){
        return AllUsersDto.builder()
                .id(user.getId())
                .fullName(user.getFirstName()+" "+user.getLastName())
                .image(user.getProfileImage())
                .joinedDate(user.getJoinedDate().toString())
                .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                .build();
    }

    public UserResponseDto toUserResponseDto(Users user){

        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                .image(user.getProfileImage())
                .build();
    }

    public Student toStudentEntity(UserRequestDto userRequestDto){

        return Student.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .profileImage(userRequestDto.getImage())
                .birthDate(LocalDate.parse(userRequestDto.getBirthDate()))
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

    public CourseInstructorDto toCourseInstructorDto(Instructor instructor, Set<UserContactDto> contactDtos){
        return CourseInstructorDto.builder()
                .id(instructor.getId())
                .fullName(instructor.getFirstName()+" "+instructor.getLastName())
                .bio(instructor.getBio())
                .yearsOfExperience(instructor.getYearsOfExperience())
                .contacts(contactDtos)
                .image(instructor.getProfileImage())
                .build();
    }

    public InstructorDto toInstructorDto(Instructor instructor
            , Set<AllCoursesDto> allCourses, Set<UserContactDto> contacts){

        long totalEnrolledStudents = instructor.getCourses()
                .stream()
                .mapToLong(course -> course.getEnrollments().size())
                .sum();

        long totalReviews = instructor.getCourses()
                .stream()
                .mapToLong(course -> course.getReviews().size())
                .sum();

        return InstructorDto.builder()
                .id(instructor.getId())
                .fullName(instructor.getFirstName()+" "+instructor.getLastName())
                .bio(instructor.getBio())
                .yearsOfExperience(instructor.getYearsOfExperience())
                .courses(allCourses)
                .totalStudents(totalEnrolledStudents)
                .totalReviews(totalReviews)
                .contacts(contacts)
                .image(instructor.getProfileImage())
                .build();
    }

    public UserContactDto toUserContactDto(UserContacts userContacts){
        return UserContactDto.builder()
                .contact(userContacts.getContact())
                .build();
    }

    public UserContacts toUserContactsEntity(UserContactDto userContactDto){
        return UserContacts.builder()
                .contact(userContactDto.getContact())
                .build();
    }

    public UpdatedUserResponseDto toUpdatedUserResponseDto(Users user, Set<UserContactDto> contactDtos){

        return UpdatedUserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .image(user.getProfileImage())
                .contacts(contactDtos)
                .build();
    }

    public SearchedInstructorDto toSearchedInstructorDto(Users user){
        return SearchedInstructorDto.builder()
                .id(user.getId())
                .fullName(user.getFirstName()+' '+user.getLastName())
                .image(user.getProfileImage())
                .build();
    }
}
