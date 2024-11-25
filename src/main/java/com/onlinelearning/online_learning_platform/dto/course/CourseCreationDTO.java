package com.onlinelearning.online_learning_platform.dto.course;

import com.onlinelearning.online_learning_platform.dto.tag.TagDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder // set for mapping
public class CourseCreationDTO {

    @NotNull(message = "Title must not be null")
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 7, message = "Title must be at least 7 characters")
    private String title;

    @NotNull(message = "Description must not be null")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Tags must not be null")
    private Set<TagDto> tags;

    @NotNull(message = "Category can't be null")
    @NotBlank(message = "Category cannot be blank")
    private String category;

    private String image;
}
