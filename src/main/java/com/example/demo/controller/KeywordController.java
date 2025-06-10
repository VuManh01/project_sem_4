package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateKeyword;
import com.example.demo.dto.response.display_for_admin.KeywordDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import com.example.demo.services.KeywordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    @GetMapping("/admin/keywords/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", keywordService.getNumberOfKeywords()), HttpStatus.OK);
    }

    @GetMapping("/admin/keywords/display")
    public ResponseEntity<List<KeywordDisplayForAdmin>> findAllForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(keywordService.getAllKeywordsForAdmin(page), HttpStatus.OK);
    }

    @PostMapping("/public/keywords")
    public ResponseEntity<Object> add(@RequestBody @Valid NewOrUpdateKeyword request) {
        try {
            NewOrUpdateKeyword keyword = keywordService.addNew(request);
            return new ResponseEntity<>(
                    Map.of(
                            "message", "Keyword added successfully",
                            "data", keyword
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

    @PutMapping("/admin/keywords/toggle/active/{id}")
    public ResponseEntity<Object> toggleKeywordActive(@PathVariable("id") int id) {
        keywordService.toggleKeywordActiveStatus(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "changes successfully"
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/public/keywords/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        keywordService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }

}
