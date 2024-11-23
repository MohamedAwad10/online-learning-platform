package com.onlinelearning.online_learning_platform.model.lesson;

import com.onlinelearning.online_learning_platform.model.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
@IdClass(LessonID.class)
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_seq")
    @SequenceGenerator(name = "lesson_seq", sequenceName = "lesson_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Id
    @ManyToOne(
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER
    )
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
            nullable = false
    )
    private String url;

    @NotNull(message = "Duration must not be null")
    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "completed")
    private boolean completed;
}
