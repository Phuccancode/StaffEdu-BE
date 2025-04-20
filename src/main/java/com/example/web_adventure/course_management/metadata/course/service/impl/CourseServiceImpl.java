package com.example.web_adventure.course_management.metadata.course.service.impl;

import com.example.web_adventure.course_management.metadata.course.dto.CourseDTO;
import com.example.web_adventure.exception.ResourceNotFoundException;
import com.example.web_adventure.old.model.Course;
import com.example.web_adventure.old.model.CourseDomain;
import com.example.web_adventure.old.model.CourseStatus;
import com.example.web_adventure.old.model.Instructor;
import com.example.web_adventure.old.repository.CourseDomainRepository;
import com.example.web_adventure.course_management.metadata.course.repository.CourseRepository;
import com.example.web_adventure.course_management.metadata.course_status.repository.CourseStatusRepository;
import com.example.web_adventure.user_management.instructor.repository.InstructorRepository;
import com.example.web_adventure.course_management.metadata.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    
    private final CourseRepository courseRepository;
    private final CourseDomainRepository domainRepository;
    private final CourseStatusRepository statusRepository;
    private final InstructorRepository instructorRepository;
    
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                           CourseDomainRepository domainRepository,
                           CourseStatusRepository statusRepository,
                           InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.domainRepository = domainRepository;
        this.statusRepository = statusRepository;
        this.instructorRepository = instructorRepository;
    }
    
    @Override
    public List<CourseDTO> getAllCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            List<CourseDTO> result = new ArrayList<>();
            
            for (Course course : courses) {
                try {
                    CourseDTO dto = convertToDTO(course);
                    result.add(dto);
                } catch (Exception e) {
                    logger.error("Error converting course with ID {} to DTO: {}", 
                        course.getId(), e.getMessage(), e);
                    // Continue with next course instead of failing the whole operation
                }
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Error in getAllCourses(): {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public Optional<CourseDTO> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);
        return convertToDTO(savedCourse);
    }
    
    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        return courseRepository.findById(id)
                .map(course -> {
                    // Update basic fields
                    course.setTitle(courseDTO.getTitle());
                    course.setDescription(courseDTO.getDescription());
                    course.setDuration(courseDTO.getDuration());
                    course.setContentDescription(courseDTO.getContentDescription());
                    course.setLearningOutcomes(courseDTO.getLearningOutcomes());
                    course.setThumbnailUrl(courseDTO.getThumbnailUrl());
                    
                    // Update relationships if IDs are provided
                    if (courseDTO.getCourseDomainId() != null) {
                        domainRepository.findById(courseDTO.getCourseDomainId())
                                .ifPresent(course::setCourseDomain);
                    }
                    
                    if (courseDTO.getStatusId() != null) {
                        statusRepository.findById(courseDTO.getStatusId())
                                .ifPresent(course::setStatus);
                    }
                    
                    if (courseDTO.getInstructorId() != null) {
                        instructorRepository.findById(courseDTO.getInstructorId())
                                .ifPresent(course::setInstructor);
                    }
                    
                    // Handle prerequisites
                    if (courseDTO.getPrerequisiteIds() != null) {
                        Set<Course> prerequisites = new HashSet<>();
                        courseDTO.getPrerequisiteIds().forEach(preId -> {
                            courseRepository.findById(preId).ifPresent(prerequisites::add);
                        });
                        course.setPrerequisites(prerequisites);
                    }
                    
                    Course updatedCourse = courseRepository.save(course);
                    return convertToDTO(updatedCourse);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }
    
    @Override
    @Transactional
    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public List<CourseDTO> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseDTO> getCoursesByDomain(Long domainId) {
        // Kiểm tra sự tồn tại của domain
        if (!domainRepository.existsById(domainId)) {
            throw new ResourceNotFoundException("Course Domain", "id", domainId);
        }
        
        return courseRepository.findByDomainId(domainId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
        // Kiểm tra sự tồn tại của instructor
        if (!instructorRepository.existsById(instructorId)) {
            throw new ResourceNotFoundException("Instructor", "id", instructorId);
        }
        
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseDTO> getCoursesByStatus(Long statusId) {
        // Kiểm tra sự tồn tại của status
        if (!statusRepository.existsById(statusId)) {
            throw new ResourceNotFoundException("Course Status", "id", statusId);
        }
        
        return courseRepository.findAll().stream()
                .filter(course -> course.getStatus() != null && course.getStatus().getId().equals(statusId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CourseDTO addPrerequisite(Long courseId, Long prerequisiteId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        Course prerequisite = courseRepository.findById(prerequisiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Prerequisite Course", "id", prerequisiteId));
        
        // Add prerequisite
        course.getPrerequisites().add(prerequisite);
        Course updatedCourse = courseRepository.save(course);
        
        return convertToDTO(updatedCourse);
    }
    
    @Override
    @Transactional
    public CourseDTO removePrerequisite(Long courseId, Long prerequisiteId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        // Check if prerequisite exists
        if (!courseRepository.existsById(prerequisiteId)) {
            throw new ResourceNotFoundException("Prerequisite Course", "id", prerequisiteId);
        }
        
        // Remove prerequisite
        course.getPrerequisites().removeIf(p -> p.getId().equals(prerequisiteId));
        Course updatedCourse = courseRepository.save(course);
        
        return convertToDTO(updatedCourse);
    }
    
    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setContentDescription(course.getContentDescription());
        dto.setLearningOutcomes(course.getLearningOutcomes());
        dto.setThumbnailUrl(course.getThumbnailUrl());
        
        // Set domain info
        if (course.getCourseDomain() != null) {
            dto.setCourseDomainId(course.getCourseDomain().getId());
            dto.setCourseDomainName(course.getCourseDomain().getName());
        }
        
        // Set status info
        if (course.getStatus() != null) {
            dto.setStatusId(course.getStatus().getId());
            dto.setStatusName(course.getStatus().getName());
        }
        
        // Set instructor info
        if (course.getInstructor() != null) {
            dto.setInstructorId(course.getInstructor().getId());
            dto.setInstructorName(course.getInstructor().getFullName());
        }
        
        // Set prerequisite IDs
        Set<Long> prerequisiteIds = course.getPrerequisites().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        dto.setPrerequisiteIds(prerequisiteIds);
        
        return dto;
    }
    
    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setDuration(dto.getDuration());
        course.setContentDescription(dto.getContentDescription());
        course.setLearningOutcomes(dto.getLearningOutcomes());
        course.setThumbnailUrl(dto.getThumbnailUrl());
        
        // Set domain
        if (dto.getCourseDomainId() != null) {
            CourseDomain domain = domainRepository.findById(dto.getCourseDomainId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course Domain", "id", dto.getCourseDomainId()));
            course.setCourseDomain(domain);
        }
        
        // Set status
        if (dto.getStatusId() != null) {
            CourseStatus status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course Status", "id", dto.getStatusId()));
            course.setStatus(status);
        }
        
        // Set instructor
        if (dto.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", dto.getInstructorId()));
            course.setInstructor(instructor);
        }
        
        // Set prerequisites
        if (dto.getPrerequisiteIds() != null && !dto.getPrerequisiteIds().isEmpty()) {
            Set<Course> prerequisites = new HashSet<>();
            dto.getPrerequisiteIds().forEach(id -> {
                courseRepository.findById(id)
                    .ifPresent(prerequisites::add);
            });
            course.setPrerequisites(prerequisites);
        }
        
        return course;
    }
}