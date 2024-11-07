package com.onlinelearning.online_learning_platform.model.lesson;

import com.onlinelearning.online_learning_platform.model.Course;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LessonID implements Serializable {
    private Integer id;
    private Course course;
}
