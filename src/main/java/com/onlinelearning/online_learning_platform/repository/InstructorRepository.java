package com.onlinelearning.online_learning_platform.repository;

import com.onlinelearning.online_learning_platform.model.Course;
import com.onlinelearning.online_learning_platform.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

    @Query("select courses from Instructor where id = :instructorId")
    List<Course> findAllCoursesById(Integer instructorId);

    @Query("select c from Instructor i JOIN i.courses c " +
            "LEFT JOIN c.tags t " +
            "where i.id = :instructorId AND " +
            "(LOWER(c.title) like LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(LOWER(c.description) like LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(LOWER(t.tagName) like LOWER(CONCAT('%', :keyword, '%')))")
    List<Course> searchInstructorCourses(Integer instructorId, String keyword);

    @Query("SELECT i from Instructor i WHERE " +
            "LOWER(i.firstName) like LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.lastName) like LOWER(CONCAT('%', :keyword, '%'))")
    List<Instructor> searchInstructors(String keyword);
}
