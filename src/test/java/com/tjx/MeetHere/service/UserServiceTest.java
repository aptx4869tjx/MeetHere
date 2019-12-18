package com.tjx.MeetHere.service;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.NewsVO;
import com.tjx.MeetHere.dao.NewsDao;
import com.tjx.MeetHere.dao.NewsImageDao;
import com.tjx.MeetHere.dao.UserDao;
import com.tjx.MeetHere.dao.UserLoginDao;
import com.tjx.MeetHere.dataObject.News;
import com.tjx.MeetHere.dataObject.NewsImage;
import com.tjx.MeetHere.dataObject.User;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.UserModel;
import com.tjx.MeetHere.service.model.UserShiro;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class UserServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserService userService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Resource
    SecurityManager securityManager;
    @Autowired
    UserDao userDao;
    @Autowired
    UserLoginDao userLoginDao;
    @Autowired
    NewsImageDao newsImageDao;
    @Autowired
    NewsDao newsDao;


    private MockMvc mockMvc;
    private Subject subject;
    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpServletResponse mockHttpServletResponse;

    //因为集成了Shiro，所以每次测试前需要在线程中绑定subject
    private void shiroLogin(String username, String password) {
        subject = new WebSubject.Builder(mockHttpServletRequest, mockHttpServletResponse).buildWebSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        subject.login(token);
        ThreadContext.bind(subject);
    }

    @BeforeEach
    void setUp() {
        mockHttpServletRequest = new MockHttpServletRequest(webApplicationContext.getServletContext());
        mockHttpServletResponse = new MockHttpServletResponse();
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        SecurityUtils.setSecurityManager(securityManager);
        shiroLogin("1002376198@qq.com", "123" + "/" + MeetHereApplication.salt);
    }

    @Test
    void getUserShiroByUserId() {
        String email = "1002376198@qq.com";
        UserShiro userShiro = userService.getUserShiroByEmail(email);
        System.out.println(userShiro.getEncryptPassword());
    }

    @ParameterizedTest
    @ValueSource(longs = {3, 164, 166})
    void getUserByUserId(long userId) {
        UserModel userModel = userService.getUserByUserId(userId);
        assertNotNull(userModel.getEmail());
        assertNotNull(userModel.getUserName());
        assertNotNull(userModel.getEncryptPassword());
        assertNotNull(userModel.getIsAdmin());
        assertNotNull(userModel.getGender());

    }

    @Test
    void getUserByUserId_1() {
        UserModel userModel = userService.getUserByUserId(null);
        assertNull(userModel);
    }


    @Test
    void getUserShiroByEmail() {
        UserShiro userShiro = userService.getUserShiroByEmail(null);
        assertNull(userShiro);
        userShiro = userService.getUserShiroByEmail("123@qq.com");
        assertNull(userShiro);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1002376198@qq.com", "10175101175@stu.ecnu.edu.cn", "1428651289@qq.com"})
    void getUserShiroByEmail_1(String email) {
        UserShiro userShiro = userService.getUserShiroByEmail(email);
        assertNotNull(userShiro.getEncryptPassword());
        assertNotNull(userShiro.getEmail());
    }

    @Test
    void register() {
        //参数值为空
        assertThrows(BusinessException.class, () -> userService.register(null));
        UserModel userModel = new UserModel();
        userModel.setEmail("1002376198@qq.com");
        userModel.setGender((byte) 0);
        userModel.setIsAdmin((byte) 0);
        userModel.setUserName("tjx");
        String password = "123";
        //参数不齐全
        assertThrows(BusinessException.class, () -> userService.register(userModel));
        userModel.setEncryptPassword(MeetHereApplication.getMD5(password));
        //参数齐全，但是邮箱重复
        assertThrows(BusinessException.class, () -> userService.register(userModel));
        userModel.setEmail("123test@qq.com");
        assertDoesNotThrow(() -> {
            userService.register(userModel);
        });
        User user = userDao.findByEmail("123test@qq.com");
        userDao.deleteByEmail("123test@qq.com");

        userLoginDao.deleteByUserId(user.getUserId());
    }

    @Test
    void validateLogin() {
        String email = "1002376198@qq.com";
        String password = "123";
        UserModel userModel = userService.validateLogin(email, password);
        assertNotNull(userModel);

        assertThrows(BusinessException.class, () -> userService.validateLogin(null, password));
        assertThrows(BusinessException.class, () -> userService.validateLogin("1234", password));
        assertThrows(BusinessException.class, () -> userService.validateLogin(email, "1234"));
    }

    @Test
    void publishNews() throws Exception {
        assertThrows(BusinessException.class, () -> userService.publishNews(null, null, null, null, null));
        assertThrows(BusinessException.class, () -> userService.publishNews(3L, null, null, null, null));
        assertThrows(BusinessException.class, () -> userService.publishNews(3L, "title", null, null, null));
        assertDoesNotThrow(() -> {
            News news = userService.publishNews(3L, "title1", "<p>测试</p>", "测试", null);
            newsDao.deleteByNewsId(news.getNewsId());
        });

        List<String> imgUrls = new ArrayList<>();
        File file = new File("/Users/tjx/Desktop/4.jpeg");
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), new FileInputStream(file));
        String imgUrl = userService.uploadNewsImage(multipartFile, "4.jpeg");
        imgUrls.add(imgUrl);
        assertDoesNotThrow(() -> {
            News news = userService.publishNews(3L, "title2", "<p>测试</p>", "测试", imgUrls);
            newsDao.deleteByNewsId(news.getNewsId());
        });

        NewsImage newsImage = newsImageDao.findByImageUrl(imgUrl);
        assertNotNull(newsImage);
        newsImageDao.deleteByImageUrl(imgUrl);

    }

    @Test
    void uploadNewsImage() throws Exception {
        File file = new File("/Users/tjx/Desktop/4.jpeg");
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), new FileInputStream(file));
        String imgUrl = userService.uploadNewsImage(multipartFile, "4.jpeg");
        NewsImage newsImage = newsImageDao.findByImageUrl(imgUrl);
        assertNotNull(newsImage);
        newsImageDao.deleteByImageUrl(imgUrl);

    }

    @ParameterizedTest
    @ValueSource(longs = {398, 403, 405})
    void getNewsVOTest(long newsId) {
        NewsVO newsVO = userService.getNewsVO(newsId);
        assertNotNull(newsVO.getContent());
    }

    @Test
    void getNewsVOTest_1() {
        Long newsId = null;
        assertThrows(BusinessException.class, () -> userService.getNewsVO(newsId));
        assertNull(userService.getNewsVO(3L));
    }

    @ParameterizedTest
    @ValueSource(ints = {0})
    void testGetNewsVO(int page) {
        List<NewsVO> newsVOList = userService.getNewsVO(page);
        assertNotNull(newsVOList);
        //System.out.println(newsVOList.toString());
    }
}