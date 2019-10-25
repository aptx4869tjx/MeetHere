package com.tjx.MeetHere.service;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.OrderModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class OrderServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    OrderService orderService;

    @Test
    void placeOrder() {
        Long userId = 3L;
        Long veuneId = 58L;
        Byte[] occupiedTimeSlots = new Byte[]{17, 18,19};
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(BusinessException.class, () -> orderService.placeOrder(null, null, null, null));
        try {
            OrderModel orderModel = orderService.placeOrder(userId, veuneId, occupiedTimeSlots, date);
        } catch (BusinessException e) {
            logger.error(e.getErrorMessage());
        }
    }
}