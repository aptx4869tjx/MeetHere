package com.tjx.MeetHere.controller.viewObject;

import java.time.LocalDateTime;

public class CommentVO {
    private Long commentId;

    private String username;

    private LocalDateTime time;

    private String venueName;

    private Byte isChecked;//0表示未审核，1表示审核通过，2表示审核不通过

    private String content;

    public CommentVO() {
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
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

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Byte getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Byte isChecked) {
        this.isChecked = isChecked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
