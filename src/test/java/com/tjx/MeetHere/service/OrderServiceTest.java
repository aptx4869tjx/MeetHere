package com.tjx.MeetHere.service;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.OrderVO;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.service.model.OrderModel;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.UUID;

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
        Long veuneId = 35L;
        Byte[] occupiedTimeSlots = new Byte[]{9, 10, 11};
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(BusinessException.class, () -> orderService.placeOrder(null, null, null, null));
        try {
            OrderModel orderModel = orderService.placeOrder(userId, veuneId, occupiedTimeSlots, date);
        } catch (BusinessException e) {
            logger.error(e.getErrorMessage());
        }
    }

    @Test
    void testFtpClient() throws IOException {
        System.out.println(testRandomUUID());
        // 1. 创建一个FtpClient对象
        FTPClient ftpClient = new FTPClient();
        // 2. 创建 ftp 连接
        ftpClient.connect("47.102.142.229", 21);
        // 3. 登录 ftp 服务器
        ftpClient.login("root", "200812");
        // 4. 读取本地文件
        FileInputStream inputStream = new FileInputStream(new File("/Users/tjx/Desktop/1.png"));
        // 5. 设置上传的路径
        System.out.println(ftpClient.changeWorkingDirectory("/html/images"));
        System.out.println(ftpClient.printWorkingDirectory());
//        ftpClient.enterLocalPassiveMode();
        // 6. 修改上传文件的格式为二进制
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        // 7. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
        ftpClient.storeFile("2.png", inputStream);
        System.out.println(ftpClient.printWorkingDirectory());
        // 8. 关闭连接
        ftpClient.logout();
    }

    @Test
    public String testRandomUUID() {
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Test
    void getOrderByUserId() {
        Long userId = 3L;
        List<OrderVO> orderVOS = orderService.getOrderByUserId(userId);
        System.out.println(orderVOS.size());
    }
}