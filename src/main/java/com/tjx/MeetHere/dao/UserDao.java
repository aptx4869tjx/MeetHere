package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    public User findByUserId(Long userId);

    public boolean existsByEmail(String email);

    public User findByEmail(String email);

    @Query(value = "select U.userId from User as U where U.email=:email")
    public Long selectUserIdByEmail(@Param("email") String email);

    @Query(value = "select U.isAdmin from User as U where U.userId=:userId")
    public byte selectRoleByUserId(@Param("userId") Long userId);

    @Query(value = "select U.email from User as U where U.userId=:userId")
    public String getEmailByUserId(@Param("userId")Long userId);

    @Query(value = "select U.userName from User as U where U.userId=:userId")
    public String getUsernameByUserId(@Param("userId")Long userId);

    @Modifying
    @Transactional
    void deleteByEmail(String email);
}
