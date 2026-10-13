package com.example.learningplatform;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "courses")
public class Course {
    @Id
    private String id;
    private String title;
    private String description;
    private int duration;
    private String instructor;
    private boolean featured;
    private List<Lesson> lessons = new ArrayList<>();

    public Course() {
    }

    public Course(String id, String title, String description, int duration, String instructor, boolean featured) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        if (lesson.getId() == null) {
            lesson.setId(new ObjectId().toHexString());
        }
        this.lessons.add(lesson);
    }

    public int getProgressPercentage() {
        if (lessons.isEmpty()) {
            return 0;
        }
        long completedLessons = lessons.stream()
                .filter(lesson -> isLessonCompleted(lesson.getId()))
                .count();
        return (int) ((completedLessons * 100.0) / lessons.size());
    }

    private boolean isLessonCompleted(String lessonId) {
        return false;
    }
}
