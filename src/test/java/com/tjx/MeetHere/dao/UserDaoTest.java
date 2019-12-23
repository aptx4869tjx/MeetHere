package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.dataObject.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByUserId() {
        User user = userDao.findByUserId(3L);
        Assertions.assertEquals("tjx",user.getUserName());
    }
}