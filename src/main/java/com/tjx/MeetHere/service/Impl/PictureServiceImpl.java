package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.PictureService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PictureServiceImpl implements PictureService {
    private String FTP_ADDRESS = "47.102.142.229";
    private Integer FTP_PORT = 21;
    private String FTP_USERNAME = "root";
    private String FTP_PASSWORD = "200812";
    private String FTP_BASE_PATH = "/html/images";
    private String IMAGE_BASE_URL = "http://47.102.142.229:/";

    String filePath = "/www/server/nginx/html/images/";

    //ftp上传到服务器
    @Override
    public String uploadPicture(MultipartFile uploadFile, String newName) throws BusinessException {
        Map<String, String> resultMap = new HashMap<>();
        boolean result = false;
        try {
            result = uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                    uploadFile.getInputStream(), FTP_BASE_PATH, newName);
            //返回结果
            if (!result) {
                throw new BusinessException(ErrorEm.PICTURE_UPLOAD_FAIL);
            }
            return IMAGE_BASE_URL+newName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorEm.PICTURE_UPLOAD_FAIL);
        }
    }


    //项目部署到服务器后，直接保存到指定目录。
    @Override
    public void uploadFile(MultipartFile uploadFile, String fileName) throws BusinessException {
        //TODO
        try {
            byte[] file = uploadFile.getBytes();
            FileUtils.writeByteArrayToFile(new File(filePath + fileName), file, false);//这里的false代表写入的文件是从头开始重新写入，或者理解为清空文件内容后重新写；若为true,则是接着原本文件内容的结尾开始写
        } catch (IOException e) {
            throw new BusinessException(ErrorEm.PICTURE_UPLOAD_FAIL);
        }
    }

    public boolean uploadFile(String ip, Integer port, String account, String passwd,
                              InputStream inputStream, String workingDir, String fileName) throws Exception {
        boolean result = false;
        // 1. 创建一个FtpClient对象
        FTPClient ftpClient = new FTPClient();
        try {
            // 2. 创建 ftp 连接
            ftpClient.connect(ip, port);
            // 3. 登录 ftp 服务器
            ftpClient.login(account, passwd);
            int reply = ftpClient.getReplyCode(); // 获取连接ftp 状态返回值
            System.out.println("code : " + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect(); // 如果返回状态不再 200 ~ 300 则认为连接失败
                return result;
            }
            ftpClient.enterLocalPassiveMode();
            // 4. 读取本地文件
//          FileInputStream inputStream = new FileInputStream(new File("F:\\hello.png"));
            // 5. 设置上传的路径
            ftpClient.changeWorkingDirectory(workingDir);
            // 6. 修改上传文件的格式为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 7. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
            if (!ftpClient.storeFile(fileName, inputStream)) {
                return result;
            }
            // 8. 关闭连接
            inputStream.close();
            ftpClient.logout();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // FIXME 听说，项目里面最好少用try catch 捕获异常，这样会导致Spring的事务回滚出问题？？？难道之前写的代码都是假代码！！！
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                    System.out.println("ftp关闭");
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}
