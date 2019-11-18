package com.tjx.MeetHere.service;

import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.UserModel;
import com.tjx.MeetHere.service.model.UserShiro;

public interface UserService {
    UserModel getUserByUserId(Long userId);

    UserShiro getUserShiroByEmail(String email);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String email, String password) throws BusinessException;
}
