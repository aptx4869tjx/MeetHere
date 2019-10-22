package com.tjx.MeetHere.service;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.VenueModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class VenueServiceTest {
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
}