package com.onlinelearning.online_learning_platform.dto.user;

import com.onlinelearning.online_learning_platform.dto.course.AllCoursesDto;
import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto extends CourseInstructorDto{

    private long totalStudents;

    private long reviews;

    private Set<AllCoursesDto> courses;
}
