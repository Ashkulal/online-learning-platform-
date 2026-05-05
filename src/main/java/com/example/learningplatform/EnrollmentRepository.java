package com.example.learningplatform;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    Optional<CourseEnrollment> findByUserIdAndCourseId(Long userId, Long courseId);
    List<CourseEnrollment> findByUserId(Long userId);
    List<CourseEnrollment> findAllByOrderByProgressPercentageDesc();
}
