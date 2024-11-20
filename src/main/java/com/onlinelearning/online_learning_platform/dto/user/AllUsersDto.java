package com.onlinelearning.online_learning_platform.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class AllUsersDto {

    private String fullName;

    private String email;

    private List<String> roles;

    private String image;

    private String joinedDate;
}
