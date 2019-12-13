package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.CommentVO;
import com.tjx.MeetHere.dataObject.Comment;
import com.tjx.MeetHere.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class CommentServiceImplTest {

    @Autowired
    CommentService commentService;

    @Test
    void saveComment() {
        String content = "123test";
        Long venueId = 35L;
        Long userId = 3L;
        CommentVO commentVO=commentService.saveComment(userId,venueId,content);

    }

    @Test
    void checkComment() {
    }
}