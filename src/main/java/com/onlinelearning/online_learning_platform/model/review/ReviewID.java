package com.onlinelearning.online_learning_platform.model.review;

import com.onlinelearning.online_learning_platform.model.Course;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReviewID implements Serializable {

    private Integer id;
    private Course course;
}
