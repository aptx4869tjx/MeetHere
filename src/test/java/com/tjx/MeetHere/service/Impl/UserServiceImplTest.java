package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.NewsVO;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.UserService;
import com.tjx.MeetHere.service.model.UserModel;
import com.tjx.MeetHere.tool.ValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class UserServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserService userService;
    @Autowired
    ValidatorImpl validator;

    @Test
    void getUserByUserId() {

        UserModel userModel = userService.getUserByUserId(3L);
        assertEquals("1002376198@qq.com", userModel.getEmail());
//        ValidationResult result = validator.validate(userModel);
//        System.out.println(result.getErrorMessage());
//        assertFalse(result.isHasError());
    }

    @Test
    void register() {
        UserModel userModel = new UserModel();
        userModel.setEmail("1002376198@qq.com");
        userModel.setGender((byte) 0);
        userModel.setIsAdmin((byte) 0);
        userModel.setUserName("tjx");
        String password = "123";
        userModel.setEncryptPassword(MeetHereApplication.getMD5(password));
        assertThrows(BusinessException.class, () -> userService.register(userModel));
        try {
            userService.register(userModel);
        } catch (BusinessException e) {
            logger.error(e.getErrorMessage());
        }


    }

    @Test
    void validateLogin() {
        String email = "1002376198@qq.com";
        String password = "123";
        assertThrows(BusinessException.class, () -> userService.validateLogin(email, "1234"));
        try {
            UserModel userModel = userService.validateLogin(email, password);
            assertEquals(userModel.getUserName(), "tjx");
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getUserShiroByEmail() {
    }

    @Test
    void publishNews() {
    }

    @Test
    void uploadNewsImage() {
    }

    @Test
    void getNewsVO() {

    }

    @Test
    void testGetNewsVO() {
        Integer page = 0;
        Long newsId = 393L;
        List<NewsVO> newsVOList = userService.getNewsVO(page);
        NewsVO newsVO = userService.getNewsVO(newsId);
        for (NewsVO n:newsVOList
             ) {
            System.out.println(n.getText());
        }
        System.out.println(newsVO.getContent());
    }
}