package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategoryName(String name);

    @Modifying
    @Query("UPDATE Course c SET c.category = NULL where c.category.id = :categoryId")
    void disassociateCoursesFromCategory(Integer categoryId);
}
