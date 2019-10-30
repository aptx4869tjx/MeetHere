package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.controller.viewObject.OrderVO;
import com.tjx.MeetHere.dao.OccupiedTimeSlotDao;
import com.tjx.MeetHere.dao.OrderInfoDao;
import com.tjx.MeetHere.dao.TimeSlotDao;
import com.tjx.MeetHere.dao.VenueDao;
import com.tjx.MeetHere.dataObject.OccupiedTimeSlot;
import com.tjx.MeetHere.dataObject.OrderInfo;
import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.OrderService;
import com.tjx.MeetHere.service.model.OrderModel;
import com.tjx.MeetHere.service.model.UserModel;
import com.tjx.MeetHere.validator.ValidationResult;
import com.tjx.MeetHere.validator.ValidatorImpl;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    ValidatorImpl validator;
    @Autowired
    VenueDao venueDao;
    @Autowired
    TimeSlotDao timeSlotDao;
    @Autowired
    OccupiedTimeSlotDao occupiedTimeSlotDao;
    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    @Transactional
    public OrderModel placeOrder(Long userId, Long venueId, Byte[] occupiedTimeSlots, LocalDate date) throws BusinessException {
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setVenueId(venueId);
        orderModel.setOccupiedTimeSlots(occupiedTimeSlots);
        orderModel.setDate(date);
        ValidationResult result = validator.validate(orderModel);
        if (result.isHasError()) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, result.getErrorMessage());
        }
        //场地不存在
        if (!venueDao.existsByVenueId(venueId)) {
            throw new BusinessException(ErrorEm.VENUE_NOT_EXIST);
        }
        //判断预约时间是否冲突
        for (Byte occupiedTimeSlot : occupiedTimeSlots
        ) {
            if (!validateTimeSlot(occupiedTimeSlot, venueId, date)) {
                throw new BusinessException(ErrorEm.VENUE_TIME_IS_OCCUPIED);
            }
        }
        orderModel.setIsChecked((byte) 0);
        orderModel.setOrderTime(LocalDateTime.now());
        OrderInfo orderInfo = getOrderInfoFromOrderModel(orderModel);
        orderInfoDao.save(orderInfo);
        orderModel.setOrderId(orderInfo.getOrderId());
        List<OccupiedTimeSlot> occupiedTimeSlots1 = getOccupiedTimeSlotsFromOrderModel(orderModel, date);
        for (OccupiedTimeSlot ots : occupiedTimeSlots1
        ) {
            occupiedTimeSlotDao.save(ots);
        }
        return orderModel;
    }

    //需要在controller层校验用户登录信息
    @Override
    public List<OrderVO> getOrderByUserId(Long userId) {
        List<OrderInfo> orderInfos = orderInfoDao.findByUserId(userId);
        if (orderInfos == null) {
            return null;
        }
        List<OrderVO> orderVOS = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfos
        ) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orderInfo, orderVO);
//            orderVO.setUsername(username);
            Venue venue = venueDao.findByVenueId(orderInfo.getVenueId());
            orderVO.setVenueName(venue.getVenueName());
            orderVO.setVenueSite(venue.getSite());
            Byte[] occupiedTimeSlots = occupiedTimeSlotDao.selectOccupiedTimeSlotsByOrderId(orderInfo.getOrderId()).toArray(Byte[]::new);
            orderVO.setSelectedSlots(occupiedTimeSlots);
            orderVOS.add(orderVO);
        }
        return orderVOS;
    }

    private boolean validateTimeSlot(Byte occupiedTimeSlot, Long venueId, LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        System.out.println(LocalDateTime.of(today, LocalTime.MIN).plusHours((long) occupiedTimeSlot - 1).toString());
        if (date.isBefore(today)) {//预约的日期在今天之前，预约无效
            return false;
        } else if (LocalDateTime.of(date, LocalTime.MIN).plusHours((long) occupiedTimeSlot - 1).isBefore(now)) {//预约的开始时间在现在之前，预约无效
            return false;
        }
        List<OccupiedTimeSlot> occupiedTimeSlots = occupiedTimeSlotDao.findByVenueIdAndDate(venueId, date);
        for (OccupiedTimeSlot ots : occupiedTimeSlots
        ) {
            if (occupiedTimeSlot.equals(ots.getOccupiedTimeSlot())) {
                return false;
            }
        }
        return true;
    }

    private OrderInfo getOrderInfoFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderModel, orderInfo);
        return orderInfo;
    }

    private List<OccupiedTimeSlot> getOccupiedTimeSlotsFromOrderModel(OrderModel orderModel, LocalDate date) {
        if (orderModel == null || date == null) {
            return null;
        }
        List<OccupiedTimeSlot> occupiedTimeSlots = new ArrayList<>();
        Long venueId = orderModel.getVenueId();
        Long orderId = orderModel.getOrderId();
        for (Byte occupiedTimeSlotId : orderModel.getOccupiedTimeSlots()
        ) {
            OccupiedTimeSlot occupiedTimeSlot = new OccupiedTimeSlot(venueId, date, orderId, occupiedTimeSlotId);
            occupiedTimeSlots.add(occupiedTimeSlot);
        }
        return occupiedTimeSlots;
    }
}
