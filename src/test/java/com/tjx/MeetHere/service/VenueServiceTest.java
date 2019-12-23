package com.tjx.MeetHere.service;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.VenueVO;
import com.tjx.MeetHere.dao.TimeSlotDao;
import com.tjx.MeetHere.dao.VenueDao;
import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.VenueModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class VenueServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    VenueService venueService;
    @Autowired
    VenueDao venueDao;
    @Autowired
    TimeSlotDao timeSlotDao;

    @Test
    void createVenue() {
        String venueName = "网球场";
        String description = "华东师范大学网球场";
        String site = "华东师范大学大学生活动中心北面楼下";
        BigDecimal price = new BigDecimal(50);
        String imgUrl = "https://tse2-mm.cn.bing.net/th?id=OIP.i5wzQPMXW_qBtwhynq0XngHaFR&w=255&h=182&c=7&o=5&dpr=2&pid=1.7";
        Byte[] timeSlots = new Byte[]{9, 10, 11, 15, 16, 17, 18, 19};
        //信息不齐全，创建场馆则应该抛出异常
        assertThrows(BusinessException.class, () -> venueService.createVenue(null, "", "", "", null, null, imgUrl));
        assertThrows(BusinessException.class, () -> venueService.createVenue(3L, "", "", "", null, null, imgUrl));

        assertDoesNotThrow(() -> {
            VenueModel venueModel = venueService.createVenue(3L, venueName, description, site, price, timeSlots, imgUrl);
            assertEquals(50.0, venueModel.getPrice().doubleValue());
            venueDao.deleteByVenueId(venueModel.getVenueId());
            timeSlotDao.deleteByVenueId(venueModel.getVenueId());
        });

    }


    @ParameterizedTest
    @ValueSource(longs = {35, 48, 58})
    void getVenueVO(Long venueId) {
        LocalDate date = LocalDate.now().plusDays(1);
        VenueVO venueVO = venueService.getVenueVO(venueId, date);
        assertNotNull(venueVO);
    }

    @Test
    void getVenueVO_1() {
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(BusinessException.class, () -> venueService.getVenueVO(null, null));
        assertThrows(BusinessException.class, () -> venueService.getVenueVO(3L, date));
    }

    @Test
    void getAllVenues() {
        List<Venue> venueList = venueService.getAllVenues();
        assertNotNull(venueList);
    }

    @Test
    void updateVenueInfo() {
        Long venueId = 35L;
        String imgUrl = "http://47.102.142.229:/007a95def3c045d691884cbc7239bbc3.png";
        Venue venue = new Venue();

        assertThrows(BusinessException.class, () -> {
            venueService.updateVenueInfo(venueId, venue, null);
        });
        venue.setVenueName("1号羽毛球场");
        assertThrows(BusinessException.class, () -> {
            venueService.updateVenueInfo(venueId, venue, null);
        });
        venue.setSite("华东师范大学 大学生活动中心二楼");
        assertThrows(BusinessException.class, () -> {
            venueService.updateVenueInfo(venueId, venue, null);
        });
        venue.setDescription("华东师范大学 大学生活动中心羽毛球场共有8片场地");
        assertThrows(BusinessException.class, () -> {
            venueService.updateVenueInfo(venueId, venue, null);
        });
        venue.setPrice(45.0);
        assertDoesNotThrow(() -> {
            venueService.updateVenueInfo(venueId, venue, null);
        });

        assertDoesNotThrow(() -> {
            venueService.updateVenueInfo(venueId, venue, imgUrl);
        });

    }
}