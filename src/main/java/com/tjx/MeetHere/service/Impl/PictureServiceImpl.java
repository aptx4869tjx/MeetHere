package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.service.PictureService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class PictureServiceImpl implements PictureService {
    private String FTP_ADDRESS = "47.102.142.229";
    private Integer FTP_PORT = 21;
    private String FTP_USERNAME="root";
    private String FTP_PASSWORD="200812";
    private String FTP_BASE_PATH="/html/images";
    private String IMAGE_BASE_URL="http://47.102.142.229:8008/";

    @Override
    public Map uploadPicture(MultipartFile uploadFile,String newName) {
        Map resultMap = new HashMap<>();
        try {
            // 1. 取原始文件名
//            String oldName = uploadFile.getOriginalFilename();

            // 2. ftp 服务器的文件名
            //String newName = oldName;
            //图片上传
            boolean result = uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                    uploadFile.getInputStream(), FTP_BASE_PATH, newName);
            //返回结果
            if(!result) {
                resultMap.put("error", "1");
                resultMap.put("message", "upload Fail");
                return resultMap;
            }
            resultMap.put("error", "0");
            resultMap.put("url", IMAGE_BASE_URL+ newName);
            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("error", "1");
            resultMap.put("message", "upload Fail");
            return resultMap;
        }
    }

    public boolean uploadFile(String ip, Integer port, String account, String passwd,
                              InputStream inputStream, String workingDir, String fileName) throws Exception{
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
        }finally {
            // FIXME 听说，项目里面最好少用try catch 捕获异常，这样会导致Spring的事务回滚出问题？？？难道之前写的代码都是假代码！！！
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}
