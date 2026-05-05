package com.example.learningplatform;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class CertificateController {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CertificateController(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/certificate/{courseId}")
    public String viewCertificate(@PathVariable Long courseId, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            return "redirect:/courses";
        }

        Optional<CourseEnrollment> enrollmentOpt = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if (enrollmentOpt.isEmpty() || enrollmentOpt.get().getCertificateId() == null) {
            return "redirect:/course/" + courseId;
        }

        CourseEnrollment enrollment = enrollmentOpt.get();

        String username = (String) session.getAttribute("username");
        String date = enrollment.getCompletionDate() != null ? 
            enrollment.getCompletionDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) :
            LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

        model.addAttribute("studentName", username);
        model.addAttribute("courseId", courseId);
        model.addAttribute("courseTitle", courseOpt.get().getTitle());
        model.addAttribute("instructorName", courseOpt.get().getInstructor());
        model.addAttribute("dateCompleted", date);
        model.addAttribute("certificateId", enrollment.getCertificateId());

        return "certificate";
    }
}
