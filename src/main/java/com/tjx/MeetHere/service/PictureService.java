package com.tjx.MeetHere.service;

import com.tjx.MeetHere.error.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface PictureService {
    String uploadPicture(MultipartFile uploadFile,String newName) throws BusinessException;
    String uploadFile(MultipartFile uploadFile, String fileName) throws BusinessException;
    String RandomUUID();
}
