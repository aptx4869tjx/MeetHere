package com.tjx.MeetHere.service;

import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.OrderModel;

import java.time.LocalDate;

public interface OrderService {
    OrderModel placeOrder(Long userId, Long venueId, Byte[] occupiedTimeSlots, LocalDate date) throws BusinessException;
}
