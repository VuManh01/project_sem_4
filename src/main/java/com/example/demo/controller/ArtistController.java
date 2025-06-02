package com.example.demo.controller;

import com.example.demo.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/admin/artists/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", artistService.getNumberOfArtist()), HttpStatus.OK);
    }
}
