package com.example.web_adventure.course_management.metadata.course.repository;

import com.example.web_adventure.old.model.Course;
import com.example.web_adventure.old.model.CourseDomain;
import com.example.web_adventure.old.model.CourseStatus;
import com.example.web_adventure.old.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByCourseDomain(CourseDomain domain);
    
    List<Course> findByStatus(CourseStatus status);
    
    List<Course> findByInstructor(Instructor instructor);
    
    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchByKeyword(String keyword);
    
    @Query("SELECT c FROM Course c WHERE c.courseDomain.id = :domainId")
    List<Course> findByDomainId(Long domainId);
    
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    List<Course> findByInstructorId(Long instructorId);
    
    @Query("SELECT c FROM Course c JOIN c.prerequisites p WHERE p.id = :prerequisiteId")
    List<Course> findByPrerequisiteId(Long prerequisiteId);
}