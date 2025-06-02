package com.example.demo.controller;

import com.example.demo.services.ViewInMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LikeAndViewController {
    @Autowired
    private ViewInMonthService service;


    @GetMapping("/public/listenInMonth/{monthId}")
    public ResponseEntity<Object> totalListenAmountInMonth(@PathVariable("monthId") int id) {
        return new ResponseEntity<>(Map.of(
                "total_listen", service.totalListenAmountInMonth(id)), HttpStatus.OK);
    }
}
