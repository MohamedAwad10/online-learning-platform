package com.onlinelearning.online_learning_platform.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {

    @NotNull(message = "Tag name cannot be null")
    @NotBlank(message = "Tag name cannot be blank")
    private String tagName;
}
