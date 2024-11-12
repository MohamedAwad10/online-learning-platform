package com.onlinelearning.online_learning_platform.model;

import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "student")
public class Student extends Users{

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(
            mappedBy = "student",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<Enrollment> enrollments;
}
