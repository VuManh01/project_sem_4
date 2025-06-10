package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateCategory;
import com.example.demo.dto.response.display_for_admin.CategoryDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import com.example.demo.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/categories/count")
    public ResponseEntity<Object> getQuantity() {
        return new ResponseEntity<>(Map.of("qty", categoryService.getNumberOfCate()), HttpStatus.OK);
    }

    @GetMapping("/admin/categories/display/{id}")
    public ResponseEntity<Object> findDetailsForAdmin(@PathVariable("id") int id) {
        CategoryDisplayForAdmin sub = categoryService.findByIdForAdmin(id);
        return new ResponseEntity<>(sub, HttpStatus.OK);
    }

    @GetMapping("/admin/categories/display")
    public ResponseEntity<List<CategoryDisplayForAdmin>> findAllForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(categoryService.getAllCategoriesDisplayForAdmin(page), HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<Object> add(@RequestBody @Valid NewOrUpdateCategory request) {
        try {
            NewOrUpdateCategory newSub = categoryService.addNewSubject(request);

            return new ResponseEntity<>(
                    Map.of(
                            "message", "Category added successfully",
                            "data", newSub
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

    @DeleteMapping("/public/categories/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }

}
