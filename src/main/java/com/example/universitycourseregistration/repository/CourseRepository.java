package com.example.universitycourseregistration.repository;

import com.example.universitycourseregistration.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByName(String name);  // Add this line
    List<Course> findByTeacherId(Long teacherId);

    @Query("SELECT c FROM Course c ORDER BY c.rating DESC")  // ← yeni: rating-ə görə descending
    List<Course> findAllByPopularity();
}