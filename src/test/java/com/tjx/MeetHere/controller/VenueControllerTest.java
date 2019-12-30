package com.tjx.MeetHere.controller;

import com.alibaba.fastjson.JSON;
import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.dao.OccupiedTimeSlotDao;
import com.tjx.MeetHere.dao.OrderInfoDao;
import com.tjx.MeetHere.dao.TimeSlotDao;
import com.tjx.MeetHere.dao.VenueDao;
import com.tjx.MeetHere.dataObject.OccupiedTimeSlot;
import com.tjx.MeetHere.dataObject.OrderInfo;
import com.tjx.MeetHere.dataObject.TimeSlot;
import com.tjx.MeetHere.dataObject.Venue;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
class VenueControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OccupiedTimeSlotDao occupiedTimeSlotDao;
    @Autowired
    private VenueDao venueDao;
    @Autowired
    private TimeSlotDao timeSlotDao;
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
    void getAllVenues() throws Exception {
        mockMvc.perform(get("/venues")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getVenueByVenueId() throws Exception {
        mockMvc.perform(get("/venue/8"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'status':'fail'}"))
                .andDo(print());
        mockMvc.perform(get("/venue/48"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'status':'success'}"))
                .andDo(print());
    }

    /**
     * 全覆盖
     * 已实现自动删除测试产生数据
     * @throws Exception
     */
    @Test
    void updateVenueStatus() throws Exception {
        //覆盖未登录异常
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2019年12月5日");
        params.put("selectedTimeSlots", new int[]{16, 17});
        mockMvc.perform(patch("/venue/35").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params)))
                .andExpect(status().isOk())
                .andDo(print());

        //正常路径
        Map<String,String> params0 = new HashMap<>();
        params0.put("email","1428651289@qq.com");
        params0.put("password","123");
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params0))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());


        Venue venue = new Venue();
        String venueName = "test" + LocalDateTime.now();
        venue.setVenueName(venueName);
        venue.setDescription("test"+ LocalDateTime.now());
        venue.setPrice(1.0);
        venue.setSite("test");
        venue.setUserId(3L);
        venue.setImgUrl("test");
        venue.setCreateTime(LocalDateTime.now());
        venueDao.save(venue);

        TimeSlot timeSlot1 = new TimeSlot(venue.getVenueId(),(byte)16);
        TimeSlot timeSlot2 = new TimeSlot(venue.getVenueId(),(byte)17);
        timeSlotDao.save(timeSlot1);
        timeSlotDao.save(timeSlot2);

        Long venueId = venue.getVenueId();

//        Map<String, Object> params = new HashMap<>();
        String date = LocalDateTime.now().plusDays(3L).format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        params.put("date", date);
        params.put("selectedTimeSlots", new int[]{16, 17});
        mockMvc.perform(patch("/venue/"+venueId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());
//        System.out.println(result.getResponse().getContentAsString());

//        LocalDate date = LocalDate.parse(params.get("date").toString(), DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
//        occupiedTimeSlotDao.
        List<OrderInfo> orderInfoList = orderInfoDao.findByVenueId(venueId);
        for(OrderInfo item : orderInfoList){
            orderInfoDao.delete(item);
        }
        List<OccupiedTimeSlot> list = occupiedTimeSlotDao.findByVenueId(venueId);
        for(OccupiedTimeSlot item : list){
            occupiedTimeSlotDao.delete(item);
        }
        timeSlotDao.deleteByVenueId(venueId);
        venueDao.delete(venue);
    }

    @Test
    /**
     * 全覆盖
     *
     */
    void createVenue() throws Exception {

        //未登录
        Map<String, Object> params1 = new HashMap<>();
        params1.put("venueName","test");
        params1.put("description","test");
        params1.put("price",1.0);
        params1.put("site","test");
        params1.put("timeSlots","16,17");
        mockMvc.perform(patch("/venue/35").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params1)))
                .andExpect(status().isOk())
                .andDo(print());


        //
        Map<String,String> params0 = new HashMap<>();
        params0.put("email","1428651289@qq.com");
        params0.put("password","123");
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(params0))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());


        File file = new File("/Users/tjx/Desktop/p.jpeg");
        MockMultipartFile multipartFile = new MockMultipartFile("photo", new FileInputStream(file));
        Map<String, Object> params = new HashMap<>();
        params.put("venueName","99988");
        params.put("description","test");
        params.put("price",1.0);
        params.put("site","test");
        params.put("timeSlots","16,17");

//        MultipartFile file = new MockMultipartFile("photo","","application/json","{\x"image\":\"/Resources/image/富江.jpg\"}".getBytes());
//        params.put("photo",file);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/venue")
                .file(multipartFile)
                .param("venueName", (String) params.get("venueName"))
                .param("description", (String) params.get("description"))
                .param("price", String.valueOf(params.get("price")))
                .param("site", (String) params.get("site"))
                .param("timeSlots", (String) params.get("timeSlots"))
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());
        List<Venue> venueList = venueDao.findByVenueName("99988");
        Venue venue = venueList.get(0);
        timeSlotDao.deleteByVenueId(venue.getVenueId());
        venueDao.deleteByVenueId(venue.getVenueId());
    }
    @Test
    void testCreateVenueWithoutLogin() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("venueName","test");
        params.put("description","test");
        params.put("price",1.0);
        params.put("site","test");
        params.put("timeSlots","16,17");
        MockMultipartFile file = new MockMultipartFile("photo","","application/json","{\"image\":\"/Resources/image/富江.jpg\"}".getBytes());
//        params.put("photo",file);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/venue")
                .file(file)
                .param("venueName", (String) params.get("venueName"))
                .param("description", (String) params.get("description"))
                .param("price", String.valueOf(params.get("price")))
                .param("site", (String) params.get("site"))
                .param("timeSlots", (String) params.get("timeSlots"))
        )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    /**
     * 使用的是ID号为1326的场馆进行测试
     */
    void updateVenueInfo() throws Exception {
        Venue venue = new Venue();
        venue.setVenueName("test");
        venue.setDescription("别删test");
        venue.setPrice(10.0);
        venue.setSite("test");
        venue.setUserId(3L);
//        venue.setImgUrl("test");
//        venue.setCreateTime(LocalDateTime.now());

        //VENUE UPDATE FAIL
        mockMvc.perform(put("/venues/1326")
                .param("venueName","乒乓球馆")
                .param("description","华东师范大学乒乓球场馆")
                .param("price",String.valueOf(venue.getPrice()))
                .param("site", "华东师范大学大学生活动中心")
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(put("/venues/1326")
                .param("venueName","乒乓球馆")
                .param("description","华东师范大学乒乓球场馆")
                .param("price",String.valueOf(venue.getPrice()))
                .param("site", "华东师范大学大学生活动中心")
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());

        //不传图片
        mockMvc.perform(put("/venues/1326")
                .param("venueName",venue.getVenueName()+LocalDateTime.now())
                .param("description",venue.getDescription()+LocalDateTime.now())
                .param("price",String.valueOf(venue.getPrice()))
                .param("site", venue.getSite())
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());

        //传图片
        File file = new File("/Users/tjx/Desktop/p.jpeg");
        MockMultipartFile multipartFile = new MockMultipartFile("photo", new FileInputStream(file));

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/venues/1326");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                .file(multipartFile)
                .param("venueName",venue.getVenueName()+LocalDateTime.now())
                .param("description",venue.getDescription()+LocalDateTime.now())
                .param("price",String.valueOf(venue.getPrice()))
                .param("site", venue.getSite())
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());

        //改回原始状态
        mockMvc.perform(put("/venues/1326")
                .param("venueName","乒乓球馆")
                .param("description","华东师范大学乒乓球场馆")
                .param("price",String.valueOf(venue.getPrice()))
                .param("site", "华东师范大学大学生活动中心")
                .session(session))
                .andExpect(status().isOk())
                .andDo(print());

    }

    /**
     * update语句返回int是什么?
     *
     * 返回值是匹配到的数据条数而不是受影响的数据条数
     *
     * 如果想要将返回int改为受影响的条数,需要在url后拼接useAffectedRows=true参数,如下
     *
     * jdbc:mysql://localhost:8000/user?useUnicode=true&useAffectedRows=true
     */
//    @Test
//    @Disabled
//    void myT(){
//        Venue venue = new Venue();
//        venue.setVenueName("test");
//        venue.setDescription("别删test");
//        venue.setPrice(1.0);
//        venue.setSite("test");
//        venue.setUserId(3L);
//
//        String venueName = venue.getVenueName();
//        String site = venue.getSite();
//        String description = venue.getDescription();
//        Double price = venue.getPrice();
//        int result = venueDao.updateVenueInfo(1326L, "test", "test", "别删test", 1.0);
//        System.out.println(result);
//        int result2 = venueDao.updateVenueInfo(1326L, "test", "test", "别删test", 1.0);
//        System.out.println(result2);
//
//
//    }

}