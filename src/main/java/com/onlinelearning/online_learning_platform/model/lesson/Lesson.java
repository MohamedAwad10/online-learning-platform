package com.onlinelearning.online_learning_platform.model.lesson;

import com.onlinelearning.online_learning_platform.model.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "lesson")
@IdClass(LessonID.class)
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull(message = "Title must not be null")
    @NotBlank(message = "Title cannot be blank")
    @Column(
            name = "title",
            nullable = false
    )
    private String title;

    @NotNull(message = "Url must not be null")
    @NotBlank(message = "Url cannot be blank")
    @Column(
            name = "url",
            nullable = false,
            unique = true
    )
    private String url;

    @Column(name = "completed")
    private boolean completed;
}
