//package com.example.web_adventure.course_management.metadata.course.service.impl;
//
//import com.example.web_adventure.course_management.metadata.course.dto.CourseDTO;
//import com.example.web_adventure.course_management.metadata.course.entity.Course;
//import com.example.web_adventure.course_management.metadata.course.repository.CourseRepository;
//import com.example.web_adventure.course_management.metadata.course_status.entity.CourseStatus;
//import com.example.web_adventure.course_management.metadata.course_status.repository.CourseStatusRepository;
//import com.example.web_adventure.exception.ResourceNotFoundException;
//import com.example.web_adventure.user_management.instructor.entity.Instructor;
//import com.example.web_adventure.user_management.instructor.repository.InstructorRepository;
//import com.example.web_adventure.course_management.metadata.course.service.CourseService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CourseServiceImpl implements CourseService {
//
//    private final CourseRepository courseRepository;
//    private final CourseStatusRepository statusRepository;
//    private final InstructorRepository instructorRepository;
//
//    @Override
//    public List<CourseDTO> getAllCourses() {
//        return courseRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<CourseDTO> getCourseById(Long id) {
//        return courseRepository.findById(id)
//                .map(this::convertToDTO);
//    }
//
//    @Override
//    @Transactional
//    public CourseDTO createCourse(CourseDTO dto) {
//        Course course = convertToEntity(dto);
//        return convertToDTO(courseRepository.save(course));
//    }
//
//    @Override
//    @Transactional
//    public CourseDTO updateCourse(Long id, CourseDTO dto) {
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
//
//        course.setCourseCode(dto.getCourseCode());
//        course.setTitle(dto.getTitle());
//        course.setDescription(dto.getDescription());
//        course.setContentDescription(dto.getContentDescription());
//        course.setLanguage(dto.getLanguage());
//        course.setStartDate(dto.getStartDate());
//        course.setEndDate(dto.getEndDate());
//        course.setEnrollmentStart(dto.getEnrollmentStart());
//        course.setEnrollmentEnd(dto.getEnrollmentEnd());
//        course.setMaxEnrollment(dto.getMaxEnrollment());
//        course.setPrice(dto.getPrice());
//        course.setCurrency(dto.getCurrency());
//        course.setDurationWeeks(dto.getDurationWeeks());
//        course.setDurationHours(dto.getDurationHours());
//        course.setExpectedTimePerWeek(dto.getExpectedTimePerWeek());
//        course.setThumbnailUrl(dto.getThumbnailUrl());
//        course.setSyllabusUrl(dto.getSyllabusUrl());
//
//        // Set relationships
//        if (dto.getStatusId() != null) {
//            CourseStatus status = statusRepository.findById(dto.getStatusId())
//                    .orElseThrow(() -> new ResourceNotFoundException("CourseStatus", "id", dto.getStatusId()));
//            course.setStatus(status);
//        }
//
//        if (dto.getInstructorId() != null) {
//            Instructor instructor = instructorRepository.findById(dto.getInstructorId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", dto.getInstructorId()));
//            course.setInstructor(instructor);
//        }
//
//        return convertToDTO(courseRepository.save(course));
//    }
//
//    @Override
//    @Transactional
//    public boolean deleteCourse(Long id) {
//        if (courseRepository.existsById(id)) {
//            courseRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public List<CourseDTO> searchCourses(String keyword) {
//        return courseRepository.searchByKeyword(keyword).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
//        if (!instructorRepository.existsById(instructorId)) {
//            throw new ResourceNotFoundException("Instructor", "id", instructorId);
//        }
//        return courseRepository.findByInstructorId(instructorId).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<CourseDTO> getCoursesByStatus(Long statusId) {
//        if (!statusRepository.existsById(statusId)) {
//            throw new ResourceNotFoundException("Course Status", "id", statusId);
//        }
//        return courseRepository.findAll().stream()
//                .filter(course -> false)
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // -------------------- DTO Mapper ------------------------
//
//    private CourseDTO convertToDTO(Course course) {
//        return CourseDTO.builder()
//                .courseId(course.getCourseId())
//                .courseCode(course.getCourseCode())
//                .title(course.getTitle())
//                .description(course.getDescription())
//                .contentDescription(course.getContentDescription())
//                .language(course.getLanguage())
//                .startDate(course.getStartDate())
//                .endDate(course.getEndDate())
//                .enrollmentStart(course.getEnrollmentStart())
//                .enrollmentEnd(course.getEnrollmentEnd())
//                .maxEnrollment(course.getMaxEnrollment())
//                .price(course.getPrice())
//                .currency(course.getCurrency())
//                .durationWeeks(course.getDurationWeeks())
//                .durationHours(course.getDurationHours())
//                .expectedTimePerWeek(course.getExpectedTimePerWeek())
//                .status(course.getStatus() != null ? course.getStatus().getStatusName() : null)
//                .instructorId(course.getInstructor() != null ? course.getInstructor().getInstructorId() : null)
//                .instructorName(course.getInstructor() != null ? course.getInstructor().getFirstName() : null)
//                .thumbnailUrl(course.getThumbnailUrl())
//                .syllabusUrl(course.getSyllabusUrl())
//                .ratingAvg(course.getRatingAvg())
//                .ratingCount(course.getRatingCount())
//                .totalEnrolled(course.getTotalEnrolled())
//                .createdAt(course.getCreatedAt())
//                .updatedAt(course.getUpdatedAt())
//                .build();
//    }
//}
