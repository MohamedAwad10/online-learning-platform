package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
}
