package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(
            name = "title",
            nullable = false
    )
    private String title;

    @Column(
            name = "url",
            nullable = false,
            unique = true
    )
    private String url;

    @Column(name = "completed")
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
