package com.tjx.MeetHere.controller;

import com.alibaba.fastjson.JSON;
import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.OrderVO;
import com.tjx.MeetHere.dao.OrderInfoDao;
import com.tjx.MeetHere.dataObject.OrderInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class OrderControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OrderInfoDao orderInfoDao;
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

    @Test
    /**
     * return未覆盖
     */
    void getUserOder() throws Exception{
        Map<String,String> params0 = new HashMap<>();
        params0.put("email","1428651289@qq.com");
        params0.put("password","123");
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .content(JSON.toJSONString(params0)))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/user/order")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page", String.valueOf(0))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    /**
     * 全覆盖
     */
    void getAllOrders() throws Exception{
        Map<String,Object> params= new HashMap<>();
        params.put("email","1428651289@qq.com");
        params.put("password","123");
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
                .content(JSON.toJSONString(params)))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/user/orders")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("page", String.valueOf(0))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    /**
     * 全覆盖
     *
     */
    void getStatistics() throws Exception{
        //没有指定场馆时
        Map<String,Object> params0 = new HashMap<>();
        mockMvc.perform(post("/user/statistics")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params0)))
                .andExpect(status().isOk())
                .andDo(print());

        //查询场馆为空
        Map<String,Object> params1 = new HashMap<>();
        params1.put("startDate", "2019年12月21日");
        params1.put("endDate", "2019年12月21日");
        params1.put("venueIdList",null);
        mockMvc.perform(post("/user/statistics")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params1)))
                .andExpect(status().isOk())
                .andDo(print());

        //正确路径
        Map<String,Object> params = new HashMap<>();
        List<Long> venueIdList = new ArrayList<>();
        venueIdList.add(35L);
        params.put("venueIdList",venueIdList);
//        params.put("startDate", LocalDateTime.parse("2019-12-12 15:00:00",
//                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        params.put("endDate",LocalDateTime.parse("2019-12-12 17:00:00",
//                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        params.put("startDate", "2019年12月21日");
        params.put("endDate", "2019年12月21日");
        mockMvc.perform(post("/user/statistics")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params)))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void testDeleteOrderByOrderId() throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(-1L);
//        orderInfo.setOrderId(-1L);
        orderInfo.setPrice(-1.0);
        orderInfo.setIsChecked((byte)0);
        orderInfo.setOrderTime(LocalDateTime.now());
        orderInfo.setVenueId(-1L);

        orderInfoDao.save(orderInfo);

        String orderId = orderInfo.getOrderId().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/order/"+orderId))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
