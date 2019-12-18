package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.controller.viewObject.OrderVO;
import com.tjx.MeetHere.dao.*;
import com.tjx.MeetHere.dataObject.OccupiedTimeSlot;
import com.tjx.MeetHere.dataObject.OrderInfo;
import com.tjx.MeetHere.dataObject.Venue;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.OrderService;
import com.tjx.MeetHere.service.model.OrderModel;
import com.tjx.MeetHere.tool.ValidationResult;
import com.tjx.MeetHere.tool.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private VenueDao venueDao;
    @Autowired
    private TimeSlotDao timeSlotDao;
    @Autowired
    private OccupiedTimeSlotDao occupiedTimeSlotDao;
    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private HttpServletRequest httpServletRequest;

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
        Venue venue = venueDao.findByVenueId(venueId);
        if (venue == null) {
            throw new BusinessException(ErrorEm.VENUE_NOT_EXIST);
        }
        orderModel.setPrice(BigDecimal.valueOf(occupiedTimeSlots.length * venue.getPrice()));
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
            OrderVO orderVO = getOrderVOFromOrderInfo(orderInfo);
            orderVOS.add(orderVO);
        }
        return orderVOS;
    }

    @Override
    public List<OrderVO> getAllOrders(Integer page) {
        if (page < 0) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "orderId");

        PageRequest pageRequest = new PageRequest(page, 5, sort);
        List<OrderInfo> orderInfos = orderInfoDao.findAll(pageRequest).getContent();
        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfos
        ) {
            OrderVO orderVO = getOrderVOFromOrderInfo(orderInfo);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    public List<Map<Object, Object>> defaultGetStatistics() {
        List<Long> venueIdList = venueDao.getAllVenueId();
        return getStatistics(venueIdList, LocalDate.now(), LocalDate.now());
    }

    //根据场地的Id和指定的天数，返回前端需要的可视化数据
    @Override
    public List<Map<Object, Object>> getStatistics(List<Long> venueIdList, LocalDate startDate, LocalDate endDate) {
        List<Map<Object, Object>> mapList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        for (Long venueId : venueIdList) {
            //获取某一个场地，指定天数的预约时段数量，将其放入map中
            Map<Object, Object> venueStatistic = new LinkedHashMap<>();
            venueStatistic.put("场地名", venueDao.getVenueNameByVenueId(venueId));

            LocalDate tmp = startDate;
            while (tmp.isBefore(endDate)) {
                venueStatistic.put(tmp.format(formatter), occupiedTimeSlotDao.getSlotCountByDateAndVenueId(tmp, venueId));
                tmp = tmp.plusDays(1);
            }
            venueStatistic.put(endDate.format(formatter), occupiedTimeSlotDao.getSlotCountByDateAndVenueId(endDate, venueId));
            mapList.add(venueStatistic);
        }
        return mapList;
    }


    private OrderVO getOrderVOFromOrderInfo(OrderInfo orderInfo) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderInfo, orderVO);
        Venue venue = venueDao.findByVenueId(orderInfo.getVenueId());
        orderVO.setUsername(userDao.getUsernameByUserId(orderInfo.getUserId()));
        orderVO.setVenueName(venue.getVenueName());
        orderVO.setVenueSite(venue.getSite());
        orderVO.setImgUrl(venue.getImgUrl());
        orderVO.setReservationDate(occupiedTimeSlotDao
                .selectReservationDateByOrderId(orderInfo.getOrderId()).get(0));
        List<Byte> bytes = occupiedTimeSlotDao
                .selectOccupiedTimeSlotsByOrderId(orderInfo.getOrderId());
        Integer[] occupiedTimeSlots = new Integer[bytes.size()];
        Stream<Byte> byteStream = bytes.stream();
        Stream<Integer> integerStream = byteStream.map(b ->
                (int) b
        );
        occupiedTimeSlots = integerStream.toArray(size -> new Integer[size]);
        orderVO.setSelectedSlots(occupiedTimeSlots);
        orderVO.setPrice(BigDecimal.valueOf(orderInfo.getPrice()));
        return orderVO;
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
        orderInfo.setPrice(orderModel.getPrice().doubleValue());
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
