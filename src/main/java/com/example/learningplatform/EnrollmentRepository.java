package com.example.learningplatform;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<CourseEnrollment, String> {
    Optional<CourseEnrollment> findByUserIdAndCourseId(String userId, String courseId);
    List<CourseEnrollment> findByUserId(String userId);
    List<CourseEnrollment> findByCourseId(String courseId);
    List<CourseEnrollment> findAllByOrderByProgressPercentageDesc();
}
