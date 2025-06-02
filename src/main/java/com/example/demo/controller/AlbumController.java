package com.example.demo.controller;

import com.example.demo.services.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AlbumController {

    @Autowired
    private AlbumService albumService;


    @GetMapping("/admin/albums/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", albumService.getNumberOfAlbum()), HttpStatus.OK);
    }

}
