package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    public User findByUserId(Long userId);
}
