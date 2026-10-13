package com.example.learningplatform;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Document(collection = "course_enrollments")
@CompoundIndex(name = "user_course_unique", def = "{'userId': 1, 'courseId': 1}", unique = true)
public class CourseEnrollment {

    @Id
    private String id;
    private String userId;
    private String username;
    private String courseId;
    private Course course;
    private int progressPercentage;
    private List<String> completedLessonIds = new ArrayList<>();
    private String certificateId;
    private LocalDate completionDate;

    public CourseEnrollment() {
    }

    public CourseEnrollment(User user, Course course, int progressPercentage) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.courseId = course.getId();
        this.course = course;
        this.progressPercentage = progressPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
        if (course != null) {
            this.courseId = course.getId();
        }
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public List<String> getCompletedLessonIds() {
        return completedLessonIds;
    }

    public void setCompletedLessonIds(List<String> completedLessonIds) {
        this.completedLessonIds = completedLessonIds;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}
