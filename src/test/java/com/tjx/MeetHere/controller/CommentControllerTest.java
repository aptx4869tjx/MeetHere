package com.tjx.MeetHere.controller;

import com.alibaba.fastjson.JSON;
import com.tjx.MeetHere.MeetHereApplication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
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
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)

class CommentControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Resource
    SecurityManager securityManager;

    private MockMvc mockMvc;
    private Subject subject;
    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpServletResponse mockHttpServletResponse;
    private MockHttpSession session;


    //因为集成了Shiro，所以每次测试前需要在线程中绑定subject
    private void shiroLogin(String username,String password){
        subject = new WebSubject.Builder(mockHttpServletRequest, mockHttpServletResponse).buildWebSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        subject.login(token);
        ThreadContext.bind(subject);
        session = new MockHttpSession();
    }

    @BeforeEach
    void setUp(){
        mockHttpServletRequest = new MockHttpServletRequest(webApplicationContext.getServletContext());
        mockHttpServletResponse = new MockHttpServletResponse();
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        SecurityUtils.setSecurityManager(securityManager);
        shiroLogin("1428651289@qq.com","123"+ "/" + MeetHereApplication.salt);
    }

    @Test
    /**
     * 全覆盖
     */
    void getCommentsByVenueId() throws Exception{
//        mockMvc.perform(get("/comments/8"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{'status':'fail'}"))
//                .andDo(print());
        mockMvc.perform(get("/comments/48"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'status':'success'}"))
                .andDo(print());
    }

    @Test
    /**
     * 全覆盖
     */
    void getAllComments() throws Exception{
        Map<String,Object> params = new HashMap<>();
        params.put("page",1);
        mockMvc.perform(get("/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page",JSON.toJSONString(1)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    /**
     * 全覆盖
     */
    void checkComment() throws Exception{
        Map<String,Object> params = new HashMap<>();
        params.put("target",1);
        mockMvc.perform(put("/comments/577")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    /**
     * (已解决） .session()
     * 最后一个语句没被覆盖
     */
    void saveComment() throws Exception{

        Map<String,String> params0 = new HashMap<>();
        params0.put("email","1428651289@qq.com");
        params0.put("password","123");
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params0)).session(session))
                .andExpect(status().isOk())
                .andDo(print());

        Map<String,Object> params = new HashMap<>();
        params.put("content","111");
//
        mockMvc.perform(post("/35/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params))
                .session(session)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }


}
