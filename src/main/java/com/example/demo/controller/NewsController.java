package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateNews;
import com.example.demo.dto.request.UpdateFileModel;
import com.example.demo.dto.response.display_for_admin.NewsDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import com.example.demo.services.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/admin/news/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", newsService.getNumberOfNews()), HttpStatus.OK);
    }

    @GetMapping("/admin/news/display")
    public ResponseEntity<List<NewsDisplayForAdmin>> findAllForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(newsService.getAllNewsForAdmin(page), HttpStatus.OK);
    }

    @PostMapping("/public/news")
    public ResponseEntity<Object> add(@RequestBody @Valid NewOrUpdateNews request) {
        try {
            NewOrUpdateNews news = newsService.addNew(request);
            return new ResponseEntity<>(
                    Map.of(
                            "message", "News added successfully",
                            "data", news
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

    @GetMapping("/admin/news/display/{id}")
    public ResponseEntity<Object> findDetailsForAdmin(@PathVariable("id") int id) {
        NewsDisplayForAdmin news = newsService.findDisplayForAdminById(id);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @DeleteMapping("/public/news/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        newsService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }


    @PutMapping("/admin/news/change/image")
    public ResponseEntity<Object> changeImage(@RequestBody @Valid UpdateFileModel request) {
        newsService.updateNewsImage(request);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/public/news")
    public ResponseEntity<Object> update(@RequestBody @Valid NewOrUpdateNews request) {
        try {
            NewOrUpdateNews updatedNews = newsService.updateNews(request);
            return new ResponseEntity<>(
                    Map.of(
                            "message", "News updated successfully",
                            "data", updatedNews
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

    @PutMapping("/admin/news/toggle/active/{id}")
    public ResponseEntity<Object> toggleNewsActive(@PathVariable("id") int id) {
        newsService.toggleNewsActiveStatus(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

}
