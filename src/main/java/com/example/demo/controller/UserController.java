package com.example.demo.controller;

import com.example.demo.dto.request.NewOrUpdateUser;
import com.example.demo.dto.response.common_response.UserResponse;
import com.example.demo.dto.response.display_for_admin.UserDisplayForAdmin;
import com.example.demo.ex.ValidationException;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/public/users")
    public ResponseEntity<List<UserResponse>> findAll(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/admin/users/display")
    public ResponseEntity<List<UserDisplayForAdmin>> findAllForAdmin(@RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(userService.getAllUsersDisplayForAdmin(page), HttpStatus.OK);
    }

    @GetMapping("/public/users/{id}")
    public ResponseEntity<Object> findDetails(@PathVariable("id") int id) {
        UserResponse album = userService.findById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("/admin/users/display/{id}")
    public ResponseEntity<Object> findDetailsForAdmin(@PathVariable("id") int id) {
        UserDisplayForAdmin album = userService.findUserDisplayForAdminById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }


    //method: edit information of user by admin
    @PutMapping("/public/users")
    public ResponseEntity<Object> update(@RequestBody @Valid NewOrUpdateUser request) {
        try {
            UserResponse updatedUser = userService.updateUser(request);

            return new ResponseEntity<>(
                    Map.of(
                            "message", "User updated successfully",
                            "data", updatedUser
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

    //method: deleted information of user by admin
    @DeleteMapping("/public/users/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        userService.deleteById(id);
        return new ResponseEntity<>(
                Map.of(
                        "message", "Deleted successfully"
                ),
                HttpStatus.OK
        );
    }


    @GetMapping("/admin/users/display/search")
    public ResponseEntity<List<UserDisplayForAdmin>> getSearchedSongsDisplayForAdmin
            (@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "searchTxt", defaultValue = "") String searchTxt) {
        return new ResponseEntity<>(userService.getSearchUserDisplayForAdmin(searchTxt, page), HttpStatus.OK);
    }
}
