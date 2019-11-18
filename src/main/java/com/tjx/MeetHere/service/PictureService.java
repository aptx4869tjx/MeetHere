package com.tjx.MeetHere.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface PictureService {
    Map uploadPicture(MultipartFile uploadFile,String newName);
    public void uploadFile(byte[] file, String fileName) throws IOException;
}
