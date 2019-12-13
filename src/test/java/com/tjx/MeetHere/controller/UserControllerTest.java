package com.tjx.MeetHere.controller;

import com.alibaba.fastjson.JSON;
import com.tjx.MeetHere.MeetHereApplication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Resource
    SecurityManager securityManager;

    private MockMvc mockMvc;
    private Subject subject;
    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpServletResponse mockHttpServletResponse;

    //因为集成了Shiro，所以每次测试前需要在线程中绑定subject
    private void shiroLogin(String username,String password){
        subject = new WebSubject.Builder(mockHttpServletRequest, mockHttpServletResponse).buildWebSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        subject.login(token);
        ThreadContext.bind(subject);
    }
    @BeforeEach
    void setUp(){
        mockHttpServletRequest = new MockHttpServletRequest(webApplicationContext.getServletContext());
        mockHttpServletResponse = new MockHttpServletResponse();
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        SecurityUtils.setSecurityManager(securityManager);
       //shiroLogin("1428651289@qq.com","123"+ "/" + MeetHereApplication.salt);
    }
    @AfterEach
    void tearDownShiro(){
        System.out.println("-------end-------");
        LifecycleUtils.destroy(securityManager);
    }

    @Test
    void register() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("username","test_user");
        params.put("email","");
        params.put("password","");
        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(JSON.toJSONString(params)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'status':'fail'}"))
                .andDo(print());
    }

    @Test
    void login() throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("email","1428651289@qq.com");
        params.put("password","123");
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params)))
                .andExpect(status().isOk())
                .andDo(print());
//        subject.logout();
//        mockMvc.perform(post("/user/login")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(JSON.toJSONString(params)))
//                .andExpect(status().isOk())
//                .andDo(print());
    }

    @Test
    void logout() {
    }
}