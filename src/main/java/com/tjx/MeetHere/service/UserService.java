package com.tjx.MeetHere.service;

import com.tjx.MeetHere.controller.viewObject.NewsVO;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.UserModel;
import com.tjx.MeetHere.service.model.UserShiro;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserModel getUserByUserId(Long userId);

    UserShiro getUserShiroByEmail(String email);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String email, String password) throws BusinessException;

    void publishNews(Long userId, String title, String content, String text, List<String> imgUrls) throws BusinessException;

    String uploadNewsImage(MultipartFile uploadFile, String newName);

    List<NewsVO> getNewsVO(Integer page);

    NewsVO getNewsVO(Long newsId);
}
