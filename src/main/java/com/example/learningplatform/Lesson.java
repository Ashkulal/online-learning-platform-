package com.example.learningplatform;

import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String description;
    private int duration; // in minutes
    private String content; // could be video URL or text content
    private String youtubeVideoId; // YouTube video ID for embedded video

    // Constructors
    public Lesson() {
    }

    public Lesson(Long id, String title, String description, int duration, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.content = content;
    }

    public Lesson(Long id, String title, String description, int duration, String content, String youtubeVideoId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.content = content;
        this.youtubeVideoId = youtubeVideoId;
    }

    public Lesson(String title, String description, int duration, String content, String youtubeVideoId) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.content = content;
        this.youtubeVideoId = youtubeVideoId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }
}