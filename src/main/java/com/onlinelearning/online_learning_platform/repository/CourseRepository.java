package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByTitle(String title);

    @Query("SELECT c FROM Course c WHERE c.status = 'PENDING'")
    List<Course> findAllPending();

    @Query("SELECT c FROM Course c WHERE c.status = 'APPROVED'")
    List<Course> findAllApproved();
}
