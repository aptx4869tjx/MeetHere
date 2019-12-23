package com.tjx.MeetHere.service;

import com.tjx.MeetHere.controller.viewObject.OrderVO;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.OrderModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderModel placeOrder(Long userId, Long venueId, Byte[] occupiedTimeSlots, LocalDate date) throws BusinessException;

    List<OrderVO> getOrderByUserId(Long userId,Integer page);

    List<OrderVO> getAllOrders(Integer page);

    //默认情况下是获取所有场馆当日的统计数据
    List<Map<Object, Object>> defaultGetStatistics();

    //根据场馆的id和起止日期获取相应的统计数据
    List<Map<Object, Object>> getStatistics(List<Long> venueIdList, LocalDate startDate, LocalDate endDate);


    void deleteOrderByOrderId(Long orderId);

}
