package com.onlinelearning.online_learning_platform.model;

import com.onlinelearning.online_learning_platform.model.enrollment.Enrollment;
import com.onlinelearning.online_learning_platform.model.lesson.Lesson;
import com.onlinelearning.online_learning_platform.model.review.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
@Builder // used for mapping
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Title must not be null")
    @NotBlank(message = "Title cannot be blank")
    @Column(
            name = "title",
            nullable = false,
            unique = true
    )
    private String title;

    @NotNull(message = "Description must not be null")
    @NotBlank(message = "Description cannot be blank")
    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDate updatedAt;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @NotNull(message = "Tags must not be null")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_tag",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<Lesson> lessons;

    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY
    )
    private Set<Review> reviews;

    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private Set<Enrollment> enrollments;
}
