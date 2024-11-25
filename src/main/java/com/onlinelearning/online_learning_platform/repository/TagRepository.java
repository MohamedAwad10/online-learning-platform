package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByTagName(String tagName);

    @Query("select tagName from Tag where LOWER(tagName) like LOWER(CONCAT('%', :keyword, '%'))")
    List<String> searchByTagName(String keyword);
}
