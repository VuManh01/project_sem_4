package com.example.demo.controller;


import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.NewOrUpdateUser;
import com.example.demo.dto.response.auth_response.AdminOrArtistLoginResponse;
import com.example.demo.dto.response.auth_response.LoginResponse;
import com.example.demo.dto.response.common_response.UserResponse;
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

    //method register tài khoản người dùng thông thường
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid NewOrUpdateUser user){
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

    //method register tài khoản admin và artist
    @PostMapping("/registerForAdmin")
    public ResponseEntity<Object> registerForAdmin(@RequestBody @Valid NewOrUpdateUser user){
        try{
            UserResponse newUser = authenticationService.registerForAdmin(user);
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

    //methed login user
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest user){
        try{
            LoginResponse loginResponse = authenticationService.verify(user);
            return new ResponseEntity<>(
                    loginResponse,
                    HttpStatus.OK
            );
        } catch (ValidationException ex){
            return new ResponseEntity<>(
                    Map.of(
                            "listErrors", ex.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    //method login for admin and artist
    @PostMapping("/loginForAdmin")
    public ResponseEntity<Object> loginAdmin(@RequestBody @Valid LoginRequest user){
        try{
            AdminOrArtistLoginResponse loginResponse = authenticationService.verifyForAdmin(user);
            return new ResponseEntity<>(
                    loginResponse,
                    HttpStatus.OK
            );
        } catch (ValidationException ex){
            return new ResponseEntity<>(
                    Map.of(
                            "listErrors", ex.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
