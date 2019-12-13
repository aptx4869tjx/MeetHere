package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long> {
    Comment findByCommentId(Long commentId);

    List<Comment> findByVenueId(Long venueId, Sort sort);

    List<Comment> findByIsChecked(Byte isChecked, Pageable pageable);
}
