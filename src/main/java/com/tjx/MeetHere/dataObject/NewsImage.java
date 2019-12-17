package com.tjx.MeetHere.dataObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NewsImage {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long newsImageId;

    private Long newsId;

    private String imageUrl;

    public NewsImage() {
    }

    public Long getNewsImageId() {
        return newsImageId;
    }

    public void setNewsImageId(Long newsImageId) {
        this.newsImageId = newsImageId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
