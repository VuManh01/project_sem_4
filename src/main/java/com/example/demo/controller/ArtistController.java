package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateArtist;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.common_response.ArtistResponse;
import com.example.demo.dto.response.display_for_admin.ArtistDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.ArtistService;

import java.util.List;
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

    @GetMapping("/admin/artists/display")
    public ResponseEntity<List<ArtistDisplayForAdmin>> getAllArtistsDisplayForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(artistService.getAllArtistsDisplayForAdmin(page), HttpStatus.OK);
    }

    @PostMapping("/public/artists")
        public ResponseEntity<Object> addNewArtist(@RequestBody @Valid NewOrUpdateArtist request) {
        try {
            NewOrUpdateArtist newArtist = artistService.addNewArtist(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newArtist);
        } catch (ValidationException e) {
            return new ResponseEntity<>(
                    Map.of(
                            "listError", e.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/admin/artists/display/{id}")
    public ResponseEntity<Object> getArtistDisplayForAdminById(@PathVariable int id) {
        ArtistDisplayForAdmin artist = artistService.findByIdForAdmin(id);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @PutMapping("/admin/artists/change/image")
    public ResponseEntity<Object> changeImage(@RequestBody @Valid UpdateFileModel request) {
        artistService.updateArtistImage(request);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/public/artists/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable int id) {
        artistService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/artists/{id}")
    public ResponseEntity<Object> getArtistById(@PathVariable int id) {
        ArtistResponse artist = artistService.findById(id);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @PutMapping("/public/artists")
    public ResponseEntity<Object> updateArtist(@RequestBody @Valid NewOrUpdateArtist request) {
        try {
            NewOrUpdateArtist updatedArtist = artistService.updateArtist(request);
            return ResponseEntity.ok(updatedArtist);
        } catch (ValidationException ex) {
            return new ResponseEntity<>(
                    Map.of(
                            "listError", ex.getErrors()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
