package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.review.Review;
import com.onlinelearning.online_learning_platform.model.review.ReviewID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, ReviewID> {

    Set<Review> findAllByCourseId(Integer courseId);

    Optional<Review> findByCourseIdAndStudentId(Integer courseId, Integer studentId);
}
