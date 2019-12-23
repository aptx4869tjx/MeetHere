package com.tjx.MeetHere.service;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.CommentVO;
import com.tjx.MeetHere.dao.CommentDao;
import com.tjx.MeetHere.error.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentDao commentDao;

    @Test
    void testSaveComment() {
        Long venueId = 35L;
        Long userId = 3L;
        String content = "test_content";
        CommentVO commentVO = commentService.saveComment(userId, venueId, content);
        assertEquals(0, commentVO.getIsChecked().intValue());
        commentDao.deleteByCommentId(commentVO.getCommentId());

        assertThrows(BusinessException.class,()->commentService.saveComment(userId,null,content));
        assertThrows(BusinessException.class,()->commentService.saveComment(userId,venueId,null));
        assertThrows(BusinessException.class,()->commentService.saveComment(userId,0L,content));
    }

    @Test
    void testCheckComment() {
        Long venueId = 35L;
        Long userId = 3L;
        String content = "test_content";
        CommentVO commentVO = commentService.saveComment(userId, venueId, content);
        commentService.checkComment(commentVO.getCommentId(),(byte)1);
        assertEquals(1,commentDao.findByCommentId(commentVO.getCommentId()).getIsChecked().intValue());
        commentDao.deleteByCommentId(commentVO.getCommentId());
        assertThrows(BusinessException.class,()->commentService.checkComment(0L,(byte)1));
    }

    @Test
    void testGetCheckedCommentVOByVenueId() {
        List<CommentVO> commentVOS = commentService.getCheckedCommentVOByVenueId(35L);
        assertNotNull(commentVOS);
    }

    @Test
    void testGetAllCommentVOByVenueId() {
    }

    @Test
    void testGetAllCommentVO() {
        Long venueId = 35L;
        Long userId = 3L;
        String content = "test_content";
        CommentVO commentVO = commentService.saveComment(userId, venueId, content);
        List<CommentVO> commentVOList = commentService.getAllCommentVO(0);
        assertNotNull(commentVOList);
        commentDao.deleteByCommentId(commentVO.getCommentId());
    }
}