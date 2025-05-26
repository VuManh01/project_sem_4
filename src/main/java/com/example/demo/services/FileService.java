package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {
    @Value("${file.upload.pjSem4-folder}")
    private String FOLDER;

    public void deleteImageFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(FOLDER + "images/" + fileName));
        } catch (IOException ex) {
            // Log lỗi nhưng không throw để tránh che dấu lỗi chính
        }
    }


    public void deleteAudioFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(FOLDER + "audio/" + fileName));
        } catch (IOException ex) {
            // Log lỗi nhưng không throw để tránh che dấu lỗi chính
        }
    }
}
