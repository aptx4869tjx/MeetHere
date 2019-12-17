package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.MeetHereApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class NewsImageDaoTest {
    @Autowired
    NewsImageDao newsImageDao;
    @Test
    void findByImageUrl() {
    }

    @Test
    void updateNewsImageByImgUrl() {
    }

    @Test
    void getImageUrlsByNewsId() {
        List<String> imgUrls = newsImageDao.getImageUrlsByNewsId(393L);
        assertEquals(1,imgUrls.size());
        for (String s:imgUrls
             ) {
            System.out.println(s);
        };
    }
}