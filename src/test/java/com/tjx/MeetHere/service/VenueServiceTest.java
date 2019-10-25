package com.tjx.MeetHere.service;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.VenueVO;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.VenueModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    @Disabled
    void createVenue() {
        String venueName = "网球场";
        String description = "华东师范大学网球场";
        String site = "华东师范大学大学生活动中心北面楼下";
        Double price = 50.0;
        String imgUrl = "https://tse2-mm.cn.bing.net/th?id=OIP.i5wzQPMXW_qBtwhynq0XngHaFR&w=255&h=182&c=7&o=5&dpr=2&pid=1.7";
        Byte[] timeSlots = new Byte[]{9, 10, 11, 15, 16, 17, 18, 19};
        assertThrows(BusinessException.class,()->venueService.createVenue("","","",null,null,imgUrl));
        try {
            VenueModel venueModel = venueService.createVenue(venueName, description, site, price, timeSlots,imgUrl);
            assertEquals(50.0,(double)venueModel.getPrice());
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getVenueVO() {
        LocalDate date =LocalDate.now().plusDays(1);
        try {
            VenueVO venueVO=venueService.getVenueVO(58L,date);
            logger.info(String.valueOf(venueVO.getTimeSlots().length));
            logger.info(String.valueOf(venueVO.getOccupiedTimeSlots().length));
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }
}