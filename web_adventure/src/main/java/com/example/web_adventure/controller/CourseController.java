package com.example.web_adventure.controller;

import com.example.web_adventure.dto.CourseDTO;
import com.example.web_adventure.exception.ResourceNotFoundException;
import com.example.web_adventure.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<CourseDTO> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            logger.error("Error fetching all courses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching courses: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        boolean deleted = courseService.deleteCourse(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(@RequestParam String keyword) {
        List<CourseDTO> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByDomain(@PathVariable Long domainId) {
        List<CourseDTO> courses = courseService.getCoursesByDomain(domainId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<CourseDTO> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByStatus(@PathVariable Long statusId) {
        List<CourseDTO> courses = courseService.getCoursesByStatus(statusId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{courseId}/prerequisites/{prerequisiteId}")
    public ResponseEntity<CourseDTO> addPrerequisite(@PathVariable Long courseId, @PathVariable Long prerequisiteId) {
        CourseDTO updatedCourse = courseService.addPrerequisite(courseId, prerequisiteId);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{courseId}/prerequisites/{prerequisiteId}")
    public ResponseEntity<CourseDTO> removePrerequisite(@PathVariable Long courseId, @PathVariable Long prerequisiteId) {
        CourseDTO updatedCourse = courseService.removePrerequisite(courseId, prerequisiteId);
        return ResponseEntity.ok(updatedCourse);
    }
}