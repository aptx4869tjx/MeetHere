package com.tjx.MeetHere.service;

import com.tjx.MeetHere.error.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface PictureService {
    String uploadPicture(MultipartFile uploadFile,String newName) throws BusinessException;
    void uploadFile(MultipartFile uploadFile, String fileName) throws BusinessException;
}
