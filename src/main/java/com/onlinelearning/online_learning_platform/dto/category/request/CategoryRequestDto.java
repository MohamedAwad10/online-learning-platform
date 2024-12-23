package com.onlinelearning.online_learning_platform.dto.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    @NotNull(message = "Category name is Required")
    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 2, message = "Category name length must be greater than 1 character")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*( [a-zA-Z]+)*$",
            message = "Category name must start with a capital letter and contain only letters")
    private String name;
}
