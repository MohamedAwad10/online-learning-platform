package com.onlinelearning.online_learning_platform.model.enrollment;

import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@Entity
@Table(name = "enrollment")
@IdClass(EnrollmentID.class)
public class Enrollment {

    @Id
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "student_id")
    private Student student;

    @Id
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "enrolled_date")
    @CreationTimestamp
    private LocalDate enrollDate;
}
