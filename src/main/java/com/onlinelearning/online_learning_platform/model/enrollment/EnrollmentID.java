package com.onlinelearning.online_learning_platform.model.enrollment;

import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Student;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EnrollmentID implements Serializable {

        private Student student;
        private Course course;
}
