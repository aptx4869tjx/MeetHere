package com.tjx.MeetHere.service;

import com.tjx.MeetHere.controller.viewObject.CommentVO;

import java.util.List;

public interface CommentService {
    CommentVO saveComment(Long userId, Long venueId, String content);

    void checkComment(Long commentId, Byte target);

    List<CommentVO> getCheckedCommentVOByVenueId(Long venueId);


    List<CommentVO> getAllCommentVO(Integer page);
}
