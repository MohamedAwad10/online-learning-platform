package com.onlinelearning.online_learning_platform.model;

import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import com.onlinelearning.online_learning_platform.model.review.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@SuperBuilder
@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Users{

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(
            mappedBy = "student",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Set<Enrollment> enrollments;

    @OneToOne(
            mappedBy = "student",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private Review review;
}
