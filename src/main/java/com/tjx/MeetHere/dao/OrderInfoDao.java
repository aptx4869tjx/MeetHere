package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderInfoDao extends JpaRepository<OrderInfo,Long> {
    OrderInfo findByOrderId(Long orderId);
    List<OrderInfo> findByUserId(Long userId);
}
