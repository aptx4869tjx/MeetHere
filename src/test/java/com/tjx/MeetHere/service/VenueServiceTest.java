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
        String venueName = "羽毛球场";
        String description = "华东师范大学 大学生活动中心羽毛球场共有8片场地";
        String site = "华东师范大学 大学生活动中心二楼";
        Double price = 30.0;
        Byte[] timeSlots = new Byte[]{9, 10, 11, 15, 16, 17, 18, 19, 20};
        assertThrows(BusinessException.class,()->venueService.createVenue("","","",null,null));
        try {
            VenueModel venueModel = venueService.createVenue(venueName, description, site, price, timeSlots);
            assertEquals(30.0,(double)venueModel.getPrice());
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getVenueVO() {
        LocalDate date =LocalDate.now().plusDays(1);
        try {
            VenueVO venueVO=venueService.getVenueVO(35L,date);
            logger.info(String.valueOf(venueVO.getTimeSlots().length));
            logger.info(String.valueOf(venueVO.getOccupiedTimeSlots().length));
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }
}