package com.onlinelearning.online_learning_platform.dto.user;

import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstructorDto {

    private Integer id;

    private String fullName;

    private String bio;

    private int yearsOfExperience;

    private Set<UserContactDto> contacts;

    private String image;
}
