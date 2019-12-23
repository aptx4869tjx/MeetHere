package com.tjx.MeetHere.service;

import com.alibaba.fastjson.JSON;
import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.OrderVO;
import com.tjx.MeetHere.dao.OccupiedTimeSlotDao;
import com.tjx.MeetHere.dao.OrderInfoDao;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.OrderModel;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetHereApplication.class)
class OrderServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    OrderService orderService;

    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    OccupiedTimeSlotDao occupiedTimeSlotDao;

//    @Test
//    void testFtpClient() throws IOException {
//        // 1. 创建一个FtpClient对象
//        FTPClient ftpClient = new FTPClient();
//        // 2. 创建 ftp 连接
//        ftpClient.connect("47.102.142.229", 21);
//        // 3. 登录 ftp 服务器
//        ftpClient.login("root", "200812");
//        // 4. 读取本地文件
//        FileInputStream inputStream = new FileInputStream(new File("/Users/tjx/Desktop/1.png"));
//        // 5. 设置上传的路径
//        System.out.println(ftpClient.changeWorkingDirectory("/html/images"));
//        System.out.println(ftpClient.printWorkingDirectory());
////        ftpClient.enterLocalPassiveMode();
//        // 6. 修改上传文件的格式为二进制
//        ftpClient.enterLocalPassiveMode();
//        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//        // 7. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
//        try {
//            ftpClient.storeFile("3.png", inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(ftpClient.printWorkingDirectory());
//        // 8. 关闭连接
//        ftpClient.logout();
//    }

    @Test
    void getOrderByUserId() {
        Long userId = 164L;
        List<OrderVO> orderVOS = orderService.getOrderByUserId(userId,0);
        System.out.println(orderVOS.size());
    }


    @Test
    void getStatistics() {
        List<Long> venueIds = new ArrayList<>();
        venueIds.add(35L);
        venueIds.add(48L);
        List<Map<Object, Object>> result = orderService.getStatistics(venueIds, LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(1));
        System.out.println(JSON.toJSONString(result));
        for (Map m : result) {
            System.out.println(m.keySet());
            System.out.println(m.values());

        }
    }

    @Test
    void defaultGetStatistics() {
        List<Map<Object, Object>> result = orderService.defaultGetStatistics();
        System.out.println(JSON.toJSONString(result));
        for (Map m : result) {
            System.out.println(m.keySet());
            System.out.println(m.values());

        }
    }

    @Test
    void testPlaceOrder() {
        Long userId = 3L;
        Long venueId = 35L;
        Byte[] occupiedTimeSlots = new Byte[]{9, 10, 11};
        LocalDate date = LocalDate.now().plusDays(1);
        //信息不全，抛出异常
        assertThrows(BusinessException.class, () -> orderService.placeOrder(null, null, null, null));
        //预约的场馆不存在，抛出异常
        assertThrows(BusinessException.class, () -> orderService.placeOrder(userId, 30L, occupiedTimeSlots, date));
        Byte[] bytes = new Byte[]{9};
        //预约时间为今天的8-9，9-10，10-11，预约的时间过期，抛出异常
        assertThrows(BusinessException.class, () -> orderService.placeOrder(userId, venueId, bytes, date.minusDays(1)));
        assertThrows(BusinessException.class, () -> orderService.placeOrder(userId, venueId, bytes, date.minusDays(2)));
        //预约的时间段不存在
        assertThrows(BusinessException.class, () -> orderService.placeOrder(userId, venueId, new Byte[]{1}, date));

        //预约的时间段冲突，抛出异常
        final Long[] orderId = new Long[1];
        assertThrows(BusinessException.class, () -> {
            OrderModel orderModel = orderService.placeOrder(userId, venueId, occupiedTimeSlots, date);
            orderId[0] = orderModel.getOrderId();
            OrderModel orderModel2 = orderService.placeOrder(userId, venueId, occupiedTimeSlots, date);
        });
        orderInfoDao.deleteByOrderId(orderId[0]);
        occupiedTimeSlotDao.deleteByOrderId(orderId[0]);
        assertDoesNotThrow(() -> {
            OrderModel orderModel = orderService.placeOrder(userId, venueId, occupiedTimeSlots, date);
            orderInfoDao.deleteByOrderId(orderModel.getOrderId());
            occupiedTimeSlotDao.deleteByOrderId(orderModel.getOrderId());
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {3, 164, 166})
    void testGetOrderByUserId(long userId) {
        List<OrderVO> orderVOList = orderService.getOrderByUserId(userId,0);
        assertNotNull(orderVOList);
    }

    @Test
    void testGetOrderByUserId_1() {
        List<OrderVO> orderVOList = orderService.getOrderByUserId(0L,0);
        assertNull(orderVOList);
    }

    @Test
    void testGetAllOrders() {
        List<OrderVO> orderVOList = orderService.getAllOrders(0);
        assertNotNull(orderVOList);
        assertEquals(5, orderVOList.size());
        assertThrows(BusinessException.class, () -> {
            orderService.getAllOrders(-1);
        });
    }

    @Test
    void testDefaultGetStatistics() {
        List<Map<Object, Object>> mapList = orderService.defaultGetStatistics();
        assertNotNull(mapList);

    }

    @Test
    void testGetStatistics() {
        List<Long> venueIds = new ArrayList<>();
        venueIds.add(35L);
        venueIds.add(48L);
        venueIds.add(58L);
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(2);
        List<Map<Object, Object>> statistics = orderService.getStatistics(venueIds, startDate, endDate);
        assertNotNull(statistics);

    }


}