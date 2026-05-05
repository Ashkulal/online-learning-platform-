package com.example.learningplatform;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String description;
    private int duration; // in hours
    private String instructor;
    private boolean featured;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private List<Lesson> lessons = new ArrayList<>();

    // Constructors
    public Course() {
    }

    public Course(Long id, String title, String description, int duration, String instructor, boolean featured) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.instructor = instructor;
        this.featured = featured;
    }

    public Course(String title, String description, int duration, String instructor, boolean featured) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.instructor = instructor;
        this.featured = featured;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    // Calculate progress percentage based on completed lessons
    public int getProgressPercentage() {
        if (lessons.isEmpty())
            return 0;
        long completedLessons = lessons.stream()
                .filter(lesson -> isLessonCompleted(lesson.getId()))
                .count();
        return (int) ((completedLessons * 100.0) / lessons.size());
    }

    // Check if a lesson is completed (this would be stored in localStorage)
    private boolean isLessonCompleted(Long lessonId) {
        // This will be handled by JavaScript/localStorage
        return false;
    }
}