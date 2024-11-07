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
    private Integer id;       // Unique identifier for the review (will not be auto-incremented)
    private Course course;    // Foreign key to Course, part of the composite key
}
