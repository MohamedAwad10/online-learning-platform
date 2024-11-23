package com.onlinelearning.online_learning_platform.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {

    @NotNull(message = "Title must not be null")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Url must not be null")
    @NotBlank(message = "Url cannot be blank")
    private String url;

    @NotNull(message = "Duration must not be null")
    private int duration;
}
