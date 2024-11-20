package com.onlinelearning.online_learning_platform.dto.user;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto extends UserDto{

    private String bio;

    private int yearsOfExperience;
}
