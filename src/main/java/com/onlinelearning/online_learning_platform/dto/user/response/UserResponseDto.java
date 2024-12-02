package com.onlinelearning.online_learning_platform.dto.user.response;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class UserResponseDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private List<String> roles;

    private String image;

}
