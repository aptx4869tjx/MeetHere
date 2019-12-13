package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.MeetHereApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class OccupiedTimeSlotDaoTest {
    @Autowired
    OccupiedTimeSlotDao occupiedTimeSlotDao;

    @Test
    void getSlotCountByDateAndVenueId() {
        System.out.println(occupiedTimeSlotDao.getSlotCountByDateAndVenueId(LocalDate.parse("2019-12-11"),48L));
        System.out.println(occupiedTimeSlotDao.getSlotCountByDateAndVenueId(LocalDate.of(2019,12,10),
                35L));
    }
}