package com.onlinelearning.online_learning_platform.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SearchedInstructorDto {

    private Integer id;
    private String fullName;
    private String image;
}
