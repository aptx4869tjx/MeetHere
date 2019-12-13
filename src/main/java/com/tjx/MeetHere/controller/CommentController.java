package com.tjx.MeetHere.controller;

import com.tjx.MeetHere.controller.viewObject.CommentVO;
import com.tjx.MeetHere.response.CommonReturnType;
import com.tjx.MeetHere.service.CommentService;
import com.tjx.MeetHere.service.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class CommentController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CommentService commentService;
    @Autowired
    HttpServletRequest httpServletRequest;

    //获取某个场地的评论的内容.如果没有venueId
    @GetMapping("/comments/{venueId}")
    public CommonReturnType getCommentsByVenueId(@PathVariable("venueId") Long venueId) {
        List<CommentVO> commentVOList;

        commentVOList = commentService.getCheckedCommentVOByVenueId(venueId);

        return new CommonReturnType(commentVOList);
    }

    //获取所有场地的未通过的评论
    @GetMapping("/comments")
    public CommonReturnType getAllComments(@RequestParam("page") Integer page) {
        List<CommentVO> commentVOList;
        commentVOList = commentService.getAllCommentVO(page);
        return new CommonReturnType(commentVOList);
    }

    //审核某条评论
    @PutMapping("/comments/{commentId}")
    public CommonReturnType checkComment(@PathVariable("commentId") Long commentId, @RequestBody Map<String, Object> params) {
        int target = (int) params.get("target");
        commentService.checkComment(commentId, (byte)target);
        return new CommonReturnType(null);
    }


    //上传评论
    @PostMapping("/{venueId}/comment")
    public CommonReturnType saveComment(@PathVariable("venueId") Long venueId, @RequestBody Map<String, Object> params) {
        String content = (String) params.get("content");
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("loginUser");
        CommentVO commentVO = commentService.saveComment(userModel.getUserId(), venueId, content);
        return new CommonReturnType(commentVO);

    }
}
