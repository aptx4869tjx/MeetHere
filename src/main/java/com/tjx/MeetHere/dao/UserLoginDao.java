package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginDao extends JpaRepository<UserLogin,Long> {
    UserLogin findByUserId(Long userId);

}
