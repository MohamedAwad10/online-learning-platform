package com.onlinelearning.online_learning_platform.model.coursesubmission;

import com.onlinelearning.online_learning_platform.model.Admin;
import com.onlinelearning.online_learning_platform.model.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_submission")
@IdClass(CourseSubmissionID.class)
public class CourseSubmission {

    @Id
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "status", nullable = false)
    private String status;
}
