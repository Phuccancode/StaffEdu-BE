package com.example.web_adventure.course_management.metadata.course.repository;

import com.example.web_adventure.course_management.metadata.course.entity.Course;
import com.example.web_adventure.course_management.metadata.course_status.entity.CourseStatus;
import com.example.web_adventure.user_management.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByStatus(CourseStatus status);
    
    List<Course> findByInstructor(Instructor instructor);
    
    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchByKeyword(String keyword);
    
    @Query("SELECT c FROM Course c WHERE c.instructor.instructorId = :instructorId")
    List<Course> findByInstructorId(Long instructorId);
}