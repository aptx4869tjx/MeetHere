package com.tjx.MeetHere.dao;

import com.tjx.MeetHere.dataObject.OrderInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface OrderInfoDao extends JpaRepository<OrderInfo,Long> {
    OrderInfo findByOrderId(Long orderId);
    List<OrderInfo> findByUserId(Long userId, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByOrderId(Long orderId);
}
