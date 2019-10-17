package com.tjx.MeetHere.controller;

import com.tjx.MeetHere.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired private UserDao userDao;
    @GetMapping("/")
    public String get(){
        return userDao.findByUserId(1L).getEmail();
    }
}
