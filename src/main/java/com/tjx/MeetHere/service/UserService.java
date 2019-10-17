package com.tjx.MeetHere.service;

import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.UserModel;

public interface UserService {
    UserModel getUserByUserId(Long userId);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String email, String password) throws BusinessException;
}
