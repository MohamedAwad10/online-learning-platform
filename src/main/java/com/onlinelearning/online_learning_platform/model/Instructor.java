package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "instructor")
public class Instructor extends Users{

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @NotNull(message = "Years of experience must not be null")
    @NotBlank(message = "Years of experience cannot be blank")
    @Column(name = "years_of_experience", nullable = false)
    private String yearsOfExperience;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses;
}
