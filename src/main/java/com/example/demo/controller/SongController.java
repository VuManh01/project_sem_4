package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateSong;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.common_response.SongResponse;
import com.example.demo.dto.response.display_for_admin.SongDisplayForAdmin;
import com.example.demo.dto.response.mix_response.SongWithViewInMonth;
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

    @GetMapping("/admin/songs/display")
    public ResponseEntity<List<SongDisplayForAdmin>> findAllSongsDisplayForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page){
        return new ResponseEntity<>(songService.getAllSongsForAdmin(page), HttpStatus.OK);
    }

    @GetMapping("/public/songs/mostListened")
    public ResponseEntity<Object> findMostListenedSongInMonth() {
        SongWithViewInMonth song = songService.getMostListenedSongInMonth();
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    //api đếm song
    @GetMapping("/admin/songs/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", songService.getNumberOfSong()), HttpStatus.OK);
    }

    @GetMapping("/public/songs/topFive")
    public ResponseEntity<Object> findTop5SongInMonth() {
        List<SongWithViewInMonth> songs = songService.getMost5ListenedSongInMonth();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/admin/songs/byUser/display/{id}")
    public ResponseEntity<List<SongDisplayForAdmin>> findAllSongsByUserIdForDisplayForAdmin
            (@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(songService.getAllFavSongsByUserIdForAdmin(id, page), HttpStatus.OK);
    }

    @GetMapping("/admin/songs/display/{id}")
    public ResponseEntity<Object> findDisplayDetailsForAdmin(@PathVariable("id") int id) {
        SongDisplayForAdmin song = songService.findDisplayForAdminById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PutMapping("/admin/songs/toggle/pending/{id}")
    public ResponseEntity<Object> toggleSongPending(@PathVariable("id") int id) {
        songService.toggleSongPendingStatus(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/admin/songs/change/rlc")
    public ResponseEntity<Object> changeRLC(@RequestBody @Valid UpdateFileModel request) {
        songService.updateSongRLC(request);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/admin/songs/change/audio")
    public ResponseEntity<Object> changeAudio(@RequestBody @Valid UpdateFileModel request) {
        songService.updateSongAudio(request);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/songs/{id}")
    public ResponseEntity<Object> findDetails(@PathVariable("id") int id) {
        SongResponse song = songService.findById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);

    }

    @DeleteMapping("/public/songs/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        songService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/songs/byPlaylist/display/{id}")
    public ResponseEntity<List<SongDisplayForAdmin>> findAllSongsByPlaylistIdForDisplayForAdmin
            (@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(songService.getAllSongsByPlaylistIdForAdmin(id, page), HttpStatus.OK);
    }

    @GetMapping("/admin/songs/byAlbum/display/{id}")
    public ResponseEntity<List<SongDisplayForAdmin>> findAllSongsByAlbumIdForDisplayForAdmin
            (@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(songService.getAllSongsByAlbumIdForAdmin(id, page), HttpStatus.OK);
    }

    @GetMapping("/admin/songs/byArtist/display/{id}")
    public ResponseEntity<List<SongDisplayForAdmin>> findAllSongsByArtistIdForDisplayForAdmin
            (@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(songService.getAllSongsByArtistIdForAdmin(id, page), HttpStatus.OK);
    }
}
