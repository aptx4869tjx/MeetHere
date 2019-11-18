package com.tjx.MeetHere.controller;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.dao.UserDao;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.response.CommonReturnType;
import com.tjx.MeetHere.service.Impl.UserServiceImpl;
import com.tjx.MeetHere.service.UserService;
import com.tjx.MeetHere.service.model.UserModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public CommonReturnType register(@RequestBody Map<String, Object> params) throws BusinessException {
        String username = (String) params.get("username");
        String email = (String) params.get("email");
        String password = (String) params.get("password");
        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setUserName(username);
        userModel.setEncryptPassword(MeetHereApplication.getMD5(password));
        userModel.setGender((byte) 0);
        userModel.setIsAdmin((byte) 0);
        userService.register(userModel);
        return new CommonReturnType(null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public CommonReturnType login(@RequestBody Map<String, Object> params) throws BusinessException {
        String email = (String) params.get("email");
        String password = (String) params.get("password");

        UserModel userModel = userService.validateLogin(email, password);
        //password = MeetHereApplication.getMD5(password);
//        UsernamePasswordToken token = new UsernamePasswordToken(email, password+"/"+MeetHereApplication.salt);
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            subject.login(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        this.httpServletRequest.getSession().setAttribute("isLogin", true);
        this.httpServletRequest.getSession().setAttribute("loginUser", userModel);
        return new CommonReturnType(userModel);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public CommonReturnType logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return new CommonReturnType(null);
    }


}
