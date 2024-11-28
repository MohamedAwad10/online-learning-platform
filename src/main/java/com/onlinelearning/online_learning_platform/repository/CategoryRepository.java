package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategoryName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Course c SET c.category.id = :dummyCategoryId where c.category.id = :oldCategoryId")
    void updateCategoryForCourse(Integer oldCategoryId, Integer dummyCategoryId);
}
