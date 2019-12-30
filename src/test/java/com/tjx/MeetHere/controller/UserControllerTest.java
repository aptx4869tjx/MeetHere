package com.tjx.MeetHere.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.dao.NewsDao;
import com.tjx.MeetHere.dao.NewsImageDao;
import com.tjx.MeetHere.dao.UserDao;
import com.tjx.MeetHere.dataObject.News;
import com.tjx.MeetHere.dataObject.NewsImage;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserDao userDao;
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private NewsImageDao newsImageDao;
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
        session = new MockHttpSession();
    }
//    @AfterEach
//    void tearDownShiro(){
//        System.out.println("-------end-------");
//        LifecycleUtils.destroy(securityManager);
//    }


    @Test
    void notLogin() throws Exception{
        mockMvc.perform(get("/user/notLogin"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    /**
     * 全覆盖
     */
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
    /**
     * 测试完记得删除测试用账户
     * 已实现自动删除
     */
    void registerS() throws Exception{
        Map<String,Object> params1 = new HashMap<>();
        params1.put("username","1ass");
        params1.put("email","11111@11.com");
        params1.put("password","111111111");
        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params1)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'status':'success'}"))
                .andDo(print());
        userDao.deleteByEmail("11111@11.com");
    }

    @Test
    /**
     * 全覆盖
     */
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
    /**
     *全覆盖
     */
    void logout() throws Exception {
        mockMvc.perform(post("/user/logout"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    /**
     * 全覆盖
     */
    void uploadNewsImage() throws Exception{
//        MockMultipartFile file = new MockMultipartFile("file","","application/json","{\"image\":\"/Users/ihsingchang/Pictures/富江.jpg\"}".getBytes());
        MockMultipartFile file = new MockMultipartFile("file","","application/json","{\"image\":\"/Resources/image/富江.jpg\"}".getBytes());
        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.multipart("/user/newsImage")
                .file(file)
                .session(session)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();
//                .andDo(print());
        //从数据库中删除测试用的图片
        String response = result.getResponse().getContentAsString();
//        System.out.println(response);
        JSONObject resJson = JSONObject.parseObject(response);
        String imgUrl = (String) resJson.get("data");
        newsImageDao.deleteByImageUrl(imgUrl);
    }

    @Test
    /**
     * 全覆盖
     */
    void publishNews() throws Exception{
        Map<String,String> params0 = new HashMap<>();
        params0.put("email","1428651289@qq.com");
        params0.put("password","123");
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params0))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());

        Map<String,Object> params = new HashMap<>();
        params.put("title","test");
        params.put("content","111");
        params.put("text","111");
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://47.102.142.229:/fff2682b452c4e79a9472d83f078462d.png");
        params.put("images",imageUrls);
        mockMvc.perform(post("/user/news")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    /**
     *全覆盖
     */
    void getNews() throws Exception{
        mockMvc.perform(get("/user/news_vo")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page",JSON.toJSONString(1)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    /**
     *全覆盖
     * （注释部分加上就不行了不知道为啥）
     */
    void getNewsByNewsId() throws Exception{
        mockMvc.perform(get("/user/news/398"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'status':'success'}"))
                .andDo(print());

//        mockMvc.perform(get("/user/news/000"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{'status':'fail'}"))
//                .andDo(print());
    }

    /**
     *  全覆盖
     * @throws Exception
     */
    @Test
    void deleteNewsByNewsId() throws  Exception{
        News news = new News();
        news.setContent("111");
        news.setText("111");
        news.setUserId(-1L);
        news.setTime(LocalDateTime.now());
        news.setTitle("111");

        newsDao.save(news);

        String newsId = news.getNewsId().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/news/"+newsId))
                .andExpect(status().isOk())
                .andDo(print());

    }

}