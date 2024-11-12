package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.dto.CourseDTO;
import com.onlinelearning.online_learning_platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByTitle(String title);
}
