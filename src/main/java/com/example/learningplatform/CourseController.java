package com.example.learningplatform;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseController(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Course> featuredCourses = courseRepository.findAll().stream()
                .filter(Course::isFeatured)
                .toList();
        model.addAttribute("featuredCourses", featuredCourses);
        return "index";
    }

    @GetMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "courses";
    }

    @GetMapping("/course/{id}")
    public String courseDetail(@PathVariable Long id, HttpSession session, Model model) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            
            Long userId = (Long) session.getAttribute("userId");
            if (userId != null) {
                Optional<CourseEnrollment> enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, id);
                if (enrollment.isPresent()) {
                    model.addAttribute("enrollment", enrollment.get());
                    model.addAttribute("completedLessonsStr", enrollment.get().getCompletedLessonIds().toString());
                }
            }
            
            return "course-detail";
        } else {
            return "error"; // You might want to create an error page
        }
    }

    @GetMapping("/my-courses")
    public String myCourses(Model model) {
        // In a real application, this would get enrolled courses from the user's
        // session/database
        // For now, we'll pass all courses and let JavaScript filter based on
        // localStorage
        model.addAttribute("allCourses", courseRepository.findAll());
        return "my-courses";
    }
}