package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserLoginDao extends JpaRepository<UserLogin, Long> {
    UserLogin findByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
