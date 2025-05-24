package com.example.demo.controller;


import com.example.demo.dto.response.NewOrUpdateUser;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.Valid;
import com.example.demo.ex.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody   @Valid NewOrUpdateUser user){
        try{
            UserResponse newUser = authenticationService.register(user);
            return new ResponseEntity<>(
                    Map.of(
                            "message", "Register successfully",
                            "data", newUser
                    ),
                    HttpStatus.OK
            );
        } catch (ValidationException e){
            return new ResponseEntity<>(
                    Map.of(
                            "listErrors", e.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
