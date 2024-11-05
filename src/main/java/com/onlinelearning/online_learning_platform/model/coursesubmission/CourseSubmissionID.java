package com.onlinelearning.online_learning_platform.model.coursesubmission;

import com.onlinelearning.online_learning_platform.model.Admin;
import com.onlinelearning.online_learning_platform.model.Course;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CourseSubmissionID implements Serializable {

    private Admin admin;
    private Course course;
}
