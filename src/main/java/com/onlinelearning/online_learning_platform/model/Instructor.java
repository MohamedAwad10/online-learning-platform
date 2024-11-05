package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "instructor")
public class Instructor extends Users{

    @Column(name = "bio")
    private String bio;

    @Column(name = "years_of_experience", nullable = false)
    private String yearsOfExperience;
}
