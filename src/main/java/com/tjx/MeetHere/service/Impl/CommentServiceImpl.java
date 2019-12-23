package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.controller.viewObject.CommentVO;
import com.tjx.MeetHere.dao.CommentDao;
import com.tjx.MeetHere.dao.UserDao;
import com.tjx.MeetHere.dao.VenueDao;
import com.tjx.MeetHere.dataObject.Comment;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private VenueDao venueDao;
    @Autowired
    private UserDao userDao;

    @Override
    public CommentVO saveComment(Long userId, Long venueId, String content) {
        //userId在controller层通过session校验，所以service层不再校验userId
        if (venueId == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        if (content == null || content.equals("")) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "内容不能为空");
        }
        if (!venueDao.existsByVenueId(venueId)) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "场地不存在");
        }
        Comment comment = new Comment();
        comment.setIsChecked((byte) 0);
        comment.setTime(LocalDateTime.now());
        comment.setContent(content);
        comment.setVenueId(venueId);
        comment.setUserId(userId);
        commentDao.save(comment);
        return getCommentVOFromComment(comment, false);
    }

    @Override
    public void checkComment(Long commentId, Byte target) {
        Comment comment = commentDao.findByCommentId(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "评论场地不存在");
        }
        comment.setIsChecked(target);
        commentDao.save(comment);
    }

    @Override
    public List<CommentVO> getCheckedCommentVOByVenueId(Long venueId) {
        Sort sort = new Sort(Sort.Direction.DESC, "commentId");
        List<Comment> commentList = commentDao.findByVenueId(venueId, sort);
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            if (comment.getIsChecked() == 1) {
                CommentVO commentVO = getCommentVOFromComment(comment, false);
                commentVOList.add(commentVO);
            }
        }
        return commentVOList;
    }


    @Override
    public List<CommentVO> getAllCommentVO(Integer page) {
        Sort sort = new Sort(Sort.Direction.DESC, "commentId");
        PageRequest pageRequest = new PageRequest(page, 5, sort);
        List<Comment> commentList = commentDao.findByIsChecked((byte) 0, pageRequest);
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = getCommentVOFromComment(comment, true);
            commentVOList.add(commentVO);
        }
        return commentVOList;
    }

    private CommentVO getCommentVOFromComment(Comment comment, boolean useVenueName) {
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        commentVO.setUsername(userDao.getUsernameByUserId(comment.getUserId()));
        if (useVenueName) {
            commentVO.setVenueName(venueDao.getVenueNameByVenueId(comment.getVenueId()));
        }
        return commentVO;
    }
}
