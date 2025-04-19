package com.example.web_adventure.old.repository;

import com.example.web_adventure.old.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    
    Optional<Instructor> findByEmail(String email);
    
    @Query("SELECT i FROM Instructor i WHERE LOWER(i.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(i.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Instructor> searchByName(String keyword);
    
    boolean existsByEmail(String email);
}