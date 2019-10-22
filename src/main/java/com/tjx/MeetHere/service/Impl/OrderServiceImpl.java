package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.dao.OccupiedTimeSlotDao;
import com.tjx.MeetHere.dao.OrderInfoDao;
import com.tjx.MeetHere.dao.TimeSlotDao;
import com.tjx.MeetHere.dao.VenueDao;
import com.tjx.MeetHere.dataObject.OccupiedTimeSlot;
import com.tjx.MeetHere.dataObject.OrderInfo;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.OrderService;
import com.tjx.MeetHere.service.model.OrderModel;
import com.tjx.MeetHere.validator.ValidationResult;
import com.tjx.MeetHere.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        //判断预约时间是否错误
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
        List<OccupiedTimeSlot> occupiedTimeSlots1 = getOccupiedTimeSlotsFromOrderModel(orderModel,date);
        for (OccupiedTimeSlot ots:occupiedTimeSlots1
             ) {
            occupiedTimeSlotDao.save(ots);
        }
        return orderModel;
    }

    private boolean validateTimeSlot(Byte occupiedTimeSlot, Long venueId, LocalDate date) {
        List<OccupiedTimeSlot> occupiedTimeSlots = occupiedTimeSlotDao.findByVenueIdAndDate(venueId, date);
        for (OccupiedTimeSlot ots : occupiedTimeSlots
        ) {
            if (occupiedTimeSlot.equals(ots.getOccupiedTimeSlot())) {
                return false;
            }
        }
        return true;
    }
    private OrderInfo getOrderInfoFromOrderModel(OrderModel orderModel){
        if(orderModel==null){
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderModel,orderInfo);
        return orderInfo;
    }
    
    private List<OccupiedTimeSlot> getOccupiedTimeSlotsFromOrderModel(OrderModel orderModel,LocalDate date){
        if(orderModel==null||date==null){
            return null;
        }
        List<OccupiedTimeSlot> occupiedTimeSlots = new ArrayList<>();
        Long venueId = orderModel.getVenueId();
        Long orderId = orderModel.getOrderId();
        for (Byte occupiedTimeSlotId:orderModel.getOccupiedTimeSlots()
             ) {
            OccupiedTimeSlot occupiedTimeSlot = new OccupiedTimeSlot(venueId,date,orderId,occupiedTimeSlotId);
            occupiedTimeSlots.add(occupiedTimeSlot);
        }
        return occupiedTimeSlots;
    }
}
