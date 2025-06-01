package com.example.demo.controller;

import com.example.demo.services.FileService;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    FileService fileService;

    @Value("${file.upload.pjSem4-folder}")
    private String FOLDER;

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImageFile(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(fileService.uploadImageFile(file));
    }
}
