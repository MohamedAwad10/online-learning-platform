package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByTagName(String tagName);

    @Query("FROM Tag WHERE LOWER(tagName) like LOWER(CONCAT('%', :keyword, '%'))")
    List<Tag> searchByTagName(String keyword);

    @Query("SELECT t FROM Tag t JOIN t.courses c WHERE c.id = :courseId AND t.id = :tagId")
    Optional<Tag> findTagInCourse(Integer courseId, Integer tagId);
}
