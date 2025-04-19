package com.example.web_adventure.old.service;

import com.example.web_adventure.old.dto.CourseDTO;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    
    List<CourseDTO> getAllCourses();
    
    Optional<CourseDTO> getCourseById(Long id);
    
    CourseDTO createCourse(CourseDTO courseDTO);
    
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    
    boolean deleteCourse(Long id);
    
    List<CourseDTO> searchCourses(String keyword);
    
    List<CourseDTO> getCoursesByDomain(Long domainId);
    
    List<CourseDTO> getCoursesByInstructor(Long instructorId);
    
    List<CourseDTO> getCoursesByStatus(Long statusId);
    
    CourseDTO addPrerequisite(Long courseId, Long prerequisiteId);
    
    CourseDTO removePrerequisite(Long courseId, Long prerequisiteId);
}