package com.onlinelearning.online_learning_platform.dto.user.response;

import com.onlinelearning.online_learning_platform.dto.user.UserContactDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class UpdatedUserResponseDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UserContactDto> contacts;
    private String image;
//    private String bio;
//    private String birthDate;
//    private int yearsOfExperience;
}
