package com.onlinelearning.online_learning_platform.model.lesson;

import com.onlinelearning.online_learning_platform.model.Course;
import jakarta.persistence.*;
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

    @Column(
            name = "title",
            nullable = false
    )
    private String title;

    @Column(
            name = "url",
            nullable = false
    )
    private String url;

    @Column(
            name = "duration",
            nullable = false
    )
    private int duration;

    @Column(name = "completed")
    private boolean completed;
}
