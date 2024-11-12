package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "instructor")
public class Instructor extends Users{

    @NotNull(message = "Bio is required")
    @Column(name = "bio", nullable = false, columnDefinition = "TEXT")
    private String bio;


    @Column(name = "years_of_experience", columnDefinition = "INT")
    private int yearsOfExperience = 0;

    @OneToMany(
            mappedBy = "instructor",
            cascade = CascadeType.MERGE, // test
            fetch = FetchType.LAZY // Use EAGER for test
    )
    private Set<Course> courses;
}
