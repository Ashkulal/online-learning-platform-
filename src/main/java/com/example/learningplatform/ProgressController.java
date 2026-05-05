package com.example.learningplatform;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public ProgressController(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProgress(@RequestParam Long courseId, 
                                            @RequestParam(required = false) Long lessonId, 
                                            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isPresent() && courseOpt.isPresent()) {
            Course course = courseOpt.get();
            Optional<CourseEnrollment> existingOpt = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
            
            CourseEnrollment enrollment;
            if (existingOpt.isPresent()) {
                enrollment = existingOpt.get();
            } else {
                enrollment = new CourseEnrollment(userOpt.get(), course, 0);
            }
            
            if (lessonId != null && !enrollment.getCompletedLessonIds().contains(lessonId)) {
                enrollment.getCompletedLessonIds().add(lessonId);
            }
            
            int totalLessons = course.getLessons().size();
            int completedLessons = enrollment.getCompletedLessonIds().size();
            int progress = totalLessons == 0 ? 0 : (int) ((completedLessons * 100.0) / totalLessons);
            
            enrollment.setProgressPercentage(progress);
            
            if (progress == 100 && enrollment.getCertificateId() == null) {
                enrollment.setCertificateId(java.util.UUID.randomUUID().toString());
                enrollment.setCompletionDate(java.time.LocalDate.now());
            }
            
            enrollmentRepository.save(enrollment);
            
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Invalid user or course");
    }
}
