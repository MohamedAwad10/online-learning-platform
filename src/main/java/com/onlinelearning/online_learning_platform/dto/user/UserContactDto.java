package com.onlinelearning.online_learning_platform.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContactDto {

    @NotNull(message = "contact cannot be null")
    @NotBlank(message = "contact cannot be blank")
    private String contact;
}
