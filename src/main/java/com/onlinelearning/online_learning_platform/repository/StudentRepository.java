package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
