package com.example.demo.dto.response.common_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

    private Integer id;
    private String username;
    private String fullName;
    private String avatar;
    private String password;
    private String phone;
    private String email;
    private String role;
    private Date dob;
    private Boolean isDeleted;
    private Date createdAt;
    private Date modifiedAt;
}
