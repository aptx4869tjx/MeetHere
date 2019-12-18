package com.tjx.MeetHere.service;

import com.tjx.MeetHere.error.BusinessException;
import org.springframework.web.multipart.MultipartFile;


public interface PictureService {
    String uploadPicture(MultipartFile uploadFile,String newName) throws BusinessException;
    String uploadFile(MultipartFile uploadFile, String fileName) throws BusinessException;
    String RandomUUID();
}
