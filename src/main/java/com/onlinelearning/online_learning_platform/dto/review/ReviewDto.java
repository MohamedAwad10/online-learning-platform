package com.onlinelearning.online_learning_platform.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ReviewDto {

    private Integer id;

    @NotNull(message = "Rating must not be null")
    @Min(value = 1, message = "Rate must greater than or equal to 1")
    @Max(value = 5, message = "Rate must lower than or equal to 5")
    private Double rate;

    private String comment;

    private String createdAt;

    private String student;
}
