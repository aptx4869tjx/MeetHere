package com.tjx.MeetHere.controller;


import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.NewsVO;

import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.response.CommonReturnType;

import com.tjx.MeetHere.service.PictureService;
import com.tjx.MeetHere.service.UserService;
import com.tjx.MeetHere.service.model.UserModel;
import org.apache.shiro.SecurityUtils;

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private PictureService pictureService;

    @RequestMapping("/notLogin")
    public CommonReturnType notLogin() {
        System.out.println("用户未登录");
        return new CommonReturnType("failed", "notLogin");
    }


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public CommonReturnType register(@RequestBody Map<String, Object> params) throws BusinessException {
        String username = (String) params.get("username");
        String email = (String) params.get("email");
        String password = (String) params.get("password");
        if(password==null||password.equals("")){
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR,"密码不能为空");
        }
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
        System.out.println("用户"+email+"验证通过");
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


    //上传新闻图片
    @PostMapping("/newsImage")
    public CommonReturnType uploadNewsImage(@RequestParam("file") MultipartFile image) {
        String newName = pictureService.RandomUUID();

        String imgUrl = userService.uploadNewsImage(image, newName + ".png");
        return new CommonReturnType(imgUrl);
    }


    //发布新闻
    @PostMapping("/news")
    public CommonReturnType publishNews(@RequestBody Map<String, Object> params) {
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        String text = (String) params.get("text");
        List<String> imageUrls = (List<String>) params.get("images");
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("loginUser");
        userService.publishNews(userModel.getUserId(), title, content, text, imageUrls);
        return new CommonReturnType(null);
    }

    @GetMapping("/news_vo")
    public CommonReturnType getNews(@RequestParam("page") Integer page) {
        List<NewsVO> newsVOList = userService.getNewsVO(page);
        return new CommonReturnType(newsVOList);
    }

    @GetMapping("/news/{newsId}")
    public CommonReturnType getNewsByNewsId(@PathVariable("newsId") Long newsId) {
        NewsVO newsVO = userService.getNewsVO(newsId);
        return new CommonReturnType(newsVO);
    }

    @DeleteMapping("/news/{newsId}")
    public CommonReturnType deleteNewsByNewsId(@PathVariable("newsId") Long newsId) {
        userService.deleteNewsByNewsId(newsId);
        return new CommonReturnType(null);
    }


}
