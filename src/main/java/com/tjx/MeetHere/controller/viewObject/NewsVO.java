package com.tjx.MeetHere.controller.viewObject;

import java.time.LocalDateTime;
import java.util.List;

public class NewsVO {

    private Long newsId;

    private String content;

    private String title;

    private String username;//发布者的姓名

    private LocalDateTime time;

    private String text;

    private List<String> imgUrls;

    public NewsVO() {
    }

    public NewsVO(Long newsId, String title, LocalDateTime time, String text) {
        this.newsId = newsId;
        this.title = title;
        this.time = time;
        this.text = text;
    }

    public NewsVO(Long newsId, String content, String title, String username, LocalDateTime time) {
        this.newsId = newsId;
        this.content = content;
        this.title = title;
        this.username = username;
        this.time = time;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }
}
