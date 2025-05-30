package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateSong;
import com.example.demo.dto.response.common_response.SongResponse;
import com.example.demo.services.SongService;
import jakarta.validation.Valid;
import com.example.demo.ex.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SongController {

    @Autowired
    private SongService songService;


    @GetMapping("/public/songs")
    public ResponseEntity<List<SongResponse>> findAll(){
        return new ResponseEntity<>(songService.getAllSongs(), HttpStatus.OK);
    }

    @PostMapping("/public/songs")
    public ResponseEntity<Object> add(@RequestBody @Valid NewOrUpdateSong request){
        try{
            NewOrUpdateSong newSong = songService.addNewSong(request);
            return new ResponseEntity<>(
                    Map.of(
                            "message", "song added successfully",
                            "data", newSong
                    ),
                    HttpStatus.OK
            );
        } catch (ValidationException e){
            return new  ResponseEntity<>(
                    Map.of(
                            "listErrors", e.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/public/songs")
    public ResponseEntity<Object> update(@RequestBody @Valid NewOrUpdateSong request){
        try{
            NewOrUpdateSong updateSong = songService.updateSong(request);

            return new ResponseEntity<>(
                    Map.of(
                            "message", "song updated successfully",
                            "data", updateSong
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
