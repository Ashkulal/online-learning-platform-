package com.example.learningplatform;

public class Lesson {
    private String id;
    private String title;
    private String description;
    private int duration;
    private String content;
    private String youtubeVideoId;

    public Lesson() {
    }

    public Lesson(String id, String title, String description, int duration, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.content = content;
    }

    public Lesson(String id, String title, String description, int duration, String content, String youtubeVideoId) {
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
