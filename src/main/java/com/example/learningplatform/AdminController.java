package com.example.learningplatform;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AdminController(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("enrollments", enrollmentRepository.findAllByOrderByProgressPercentageDesc());
        return "admin-dashboard";
    }

    @GetMapping("/course/add")
    public String showAddCourseForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("course", new Course());
        return "admin-course-form";
    }

    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        // If course is new, it has no lessons, initialize empty list
        if (course.getId() != null) {
            Optional<Course> existing = courseRepository.findById(course.getId());
            if (existing.isPresent()) {
                Course existingCourse = existing.get();
                existingCourse.setTitle(course.getTitle());
                existingCourse.setDescription(course.getDescription());
                existingCourse.setInstructor(course.getInstructor());
                existingCourse.setDuration(course.getDuration());
                existingCourse.setFeatured(course.isFeatured());
                courseRepository.save(existingCourse);
                return "redirect:/admin/dashboard?success=updated";
            }
        }
        
        courseRepository.save(course);
        return "redirect:/admin/dashboard?success=added";
    }

    @GetMapping("/course/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "admin-course-form";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        courseRepository.deleteById(id);
        return "redirect:/admin/dashboard?success=deleted";
    }
}
