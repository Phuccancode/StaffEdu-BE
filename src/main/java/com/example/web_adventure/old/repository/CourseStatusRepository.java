package com.example.web_adventure.old.repository;

import com.example.web_adventure.old.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseStatusRepository extends JpaRepository<CourseStatus, Long> {
    
    Optional<CourseStatus> findByName(String name);
    
    boolean existsByName(String name);
}