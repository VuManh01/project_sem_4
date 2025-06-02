package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateGenres;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.display_for_admin.GenreDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import com.example.demo.services.GenreSongService;
import com.example.demo.services.GenresService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GenresController {

    @Autowired
    private GenresService genresService;

    //method add genres by admin or artist
    @PostMapping("/public/genres")
    public ResponseEntity<Object> add(@RequestBody @Valid NewOrUpdateGenres request){
        try{
            NewOrUpdateGenres newGenre = genresService.addNewGenre(request);
            return new ResponseEntity<>(
                    Map.of(
                            "message", "Genres added successfully",
                            "data", newGenre
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
//method show detail genres by admin
    @GetMapping("/admin/genres/display/{id}")
    public ResponseEntity<Object> findDetailsForAdmin(@PathVariable("id") int id){
        GenreDisplayForAdmin genre = genresService.findGenreDisplayForAdminById(id);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @GetMapping("/admin/genres/display")
    public ResponseEntity<List<GenreDisplayForAdmin>> findAllGenreDisplayForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(genresService.getAllGenreDisplayForAdmin(page), HttpStatus.OK);
    }

    //method change images
    @PutMapping("/admin/genres/change/image")
    public ResponseEntity<Object> changeImage(@RequestBody @Valid UpdateFileModel request) {
        genresService.updateGenreImage(request);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/genres/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", genresService.getNumberOfGenre()), HttpStatus.OK);
    }
}
