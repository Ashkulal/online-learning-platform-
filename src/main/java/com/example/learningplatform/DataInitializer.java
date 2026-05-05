package com.example.learningplatform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public DataInitializer(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create default admin user or ensure existing one has ADMIN role and correct password
        Optional<User> adminOpt = userRepository.findByUsername("admin");
        if (adminOpt.isEmpty()) {
            User admin = new User("admin", "9480", "ADMIN");
            userRepository.save(admin);
        } else {
            User admin = adminOpt.get();
            admin.setRole("ADMIN");
            admin.setPassword("9480");
            userRepository.save(admin);
        }

        if (courseRepository.count() == 0) {
            Course course1 = new Course("Introduction to Java", "Learn the basics of Java programming", 10, "John Doe", true);
            course1.addLesson(new Lesson("Getting Started with Java", "Introduction to Java programming language", 30, "video1.mp4", "eIrMbAQSU34"));
            course1.addLesson(new Lesson("Variables and Data Types", "Understanding variables and data types in Java", 45, "video2.mp4", "8KMwI6ISz4o"));
            course1.addLesson(new Lesson("Control Structures", "Learn about if-else, loops, and switch statements", 60, "video3.mp4", "4k6xR3BJQks"));
            course1.addLesson(new Lesson("Object-Oriented Programming", "Introduction to classes and objects", 75, "video4.mp4", "1ONhXmQuWP8"));
            courseRepository.save(course1);

            Course course2 = new Course("Web Development with Spring", "Build web applications using Spring Framework", 15, "Jane Smith", true);
            course2.addLesson(new Lesson("Spring Framework Overview", "Introduction to Spring Framework", 40, "video5.mp4", "vtPkZShrvXQ"));
            course2.addLesson(new Lesson("Spring MVC Basics", "Understanding Spring MVC architecture", 55, "video6.mp4", "KbhmfKjPR2M"));
            course2.addLesson(new Lesson("RESTful Web Services", "Building REST APIs with Spring", 70, "video7.mp4", "9SGDpanrc8U"));
            course2.addLesson(new Lesson("Spring Security", "Securing your Spring applications", 65, "video8.mp4", "her_7pa0vrg"));
            courseRepository.save(course2);

            Course course3 = new Course("Data Structures and Algorithms", "Master fundamental data structures and algorithms", 20, "Bob Johnson", false);
            course3.addLesson(new Lesson("Arrays and Strings", "Working with arrays and string manipulation", 50, "video9.mp4", "vBc7T7d_yWk"));
            course3.addLesson(new Lesson("Linked Lists", "Understanding linked list data structure", 60, "video10.mp4", "0uZvQZGXn9M"));
            course3.addLesson(new Lesson("Stacks and Queues", "Learn about stack and queue data structures", 55, "video11.mp4", "Gp2bVxW5rXc"));
            course3.addLesson(new Lesson("Sorting Algorithms", "Common sorting algorithms and their implementations", 80, "video12.mp4", "bUNkvhFXfWw"));
            courseRepository.save(course3);

            Course course4 = new Course("Machine Learning Basics", "Introduction to machine learning concepts", 12, "Alice Brown", false);
            course4.addLesson(new Lesson("What is Machine Learning?", "Overview of machine learning", 35, "video13.mp4", "ukzFI9rgwfU"));
            course4.addLesson(new Lesson("Supervised Learning", "Understanding supervised learning algorithms", 50, "video14.mp4", "cfj6yaYE86U"));
            course4.addLesson(new Lesson("Unsupervised Learning", "Exploring unsupervised learning techniques", 45, "video15.mp4", "1XqG0kaJVHY"));
            course4.addLesson(new Lesson("Model Evaluation", "How to evaluate machine learning models", 40, "video16.mp4", "Vpcl3DlP2xo"));
            courseRepository.save(course4);
        }
    }
}
