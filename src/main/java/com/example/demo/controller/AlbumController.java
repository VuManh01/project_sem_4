package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateAlbum;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.auth_response.AlbumDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import com.example.demo.services.AlbumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/admin/albums/display")
    public ResponseEntity<List<AlbumDisplayForAdmin>> findAllAlbumsDisplayForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(albumService.getAllAlbumsDisplayForAdmin(page), HttpStatus.OK);
    }

    @PostMapping("/public/albums")
    public ResponseEntity<Object> add(@RequestBody @Valid NewOrUpdateAlbum request) {
        try {
            NewOrUpdateAlbum newAlbum = albumService.addNewAlbum(request);

            return new ResponseEntity<>(
                    Map.of(
                            "message", "Album added successfully",
                            "data", newAlbum
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

    @GetMapping("/admin/albums/byUser/display/{id}")
    public ResponseEntity<List<AlbumDisplayForAdmin>> findAllAlbumsByUserIdForAdmin
            (@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(albumService.getAllFavAlbumsByUserIdForAdmin(id, page), HttpStatus.OK);
    }

    @GetMapping("/admin/albums/display/{id}")
    public ResponseEntity<Object> findDisplayDetailsForAdmin(@PathVariable("id") int id) {
        AlbumDisplayForAdmin album = albumService.findDisplayForAdminById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PutMapping("/admin/albums/change/image")
    public ResponseEntity<Object> changeImage(@RequestBody @Valid UpdateFileModel request) {
        albumService.updateAlbumImage(request);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/admin/albums/toggle/release/{id}")
    public ResponseEntity<Object> toggleReleaseAlbum(@PathVariable("id") int id) {
        albumService.toggleAlbumReleaseStatus(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/public/albums/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        albumService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/albums/byArtist/display/{id}")
    public ResponseEntity<List<AlbumDisplayForAdmin>> findAllAlbumsByArtistIdForAdmin
            (@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(albumService.getAllAlbumsByArtistIdForAdmin(id, page), HttpStatus.OK);
    }

    @GetMapping("/admin/albums/byCategory/display/{id}")
    public ResponseEntity<List<AlbumDisplayForAdmin>> findAllAlbumsBySubjectIdForDisplay
            (@PathVariable("id") int id, @RequestParam(value = "page", defaultValue = "0") int page) {
        List<AlbumDisplayForAdmin> album = albumService.getAllAlbumsBySubjectIdForAdmin(id, page);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }
}
