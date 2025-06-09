package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdatePlaylist;
import com.example.demo.dto.response.common_response.PlaylistResponse;
import com.example.demo.dto.response.display_for_admin.PlaylistDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import com.example.demo.services.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/admin/playlists/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", playlistService.getNumberOfPlaylist()), HttpStatus.OK);
    }

    @GetMapping("/admin/playlists/display")
    public ResponseEntity<List<PlaylistDisplayForAdmin>> findAllPlaylistsDisplayForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(playlistService.getAllPlaylistsDisplayForAdmin(page), HttpStatus.OK);
    }

    @PostMapping("/public/playlists")
    public ResponseEntity<Object> add(@RequestBody @Valid NewOrUpdatePlaylist request) {
        try {

            NewOrUpdatePlaylist newPlaylist = playlistService.addNewPlaylist(request);
            return new ResponseEntity<>(
                    Map.of(
                            "message", "Playlist added successfully",
                            "data", newPlaylist
                    ),
                    HttpStatus.OK
            );
        } catch (ValidationException e) {
            return new ResponseEntity<>(
                    Map.of(
                            "listError", e.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/public/playlists")
    public ResponseEntity<Object> update(@RequestBody @Valid NewOrUpdatePlaylist request) {
        try {

            NewOrUpdatePlaylist updatedPlaylist = playlistService.updatePlaylist(request);

            return new ResponseEntity<>(
                    Map.of(
                            "message", "Playlist updated successfully",
                            "data", updatedPlaylist
                    ),
                    HttpStatus.OK
            );
        } catch (ValidationException e) {
            return new ResponseEntity<>(
                    Map.of(
                            "listError", e.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/admin/playlists/display/search")
    public ResponseEntity<List<PlaylistDisplayForAdmin>> getSearchedPlaylistsDisplayForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "searchTxt", defaultValue = "") String searchTxt) {
        return new ResponseEntity<>(playlistService.getSearchPlaylistsDisplayForAdmin(searchTxt, page), HttpStatus.OK);
    }

    @GetMapping("/public/playlists/{id}")
    public ResponseEntity<Object> findDetails(@PathVariable("id") int id) {
        PlaylistResponse playlist = playlistService.findById(id);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @GetMapping("/admin/playlists/display/{id}")
    public ResponseEntity<Object> findDisplayDetailsForAdmin(@PathVariable("id") int id) {
        PlaylistDisplayForAdmin playlist = playlistService.findDisplayForAdminById(id);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @DeleteMapping("/public/playlists/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        playlistService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }
}
