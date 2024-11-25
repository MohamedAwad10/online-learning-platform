package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "instructor")
public class Instructor extends Users{

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "years_of_experience", columnDefinition = "INT")
    private int yearsOfExperience;

    @OneToMany(
            mappedBy = "instructor",
            fetch = FetchType.EAGER // Use EAGER for test
    )
    private Set<Course> courses;
}
