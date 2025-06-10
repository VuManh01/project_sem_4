package com.example.demo.dto.response.display_for_admin;

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
public class NewsDisplayForAdmin implements Serializable {

    private Integer id;
    private String title;
    private String content;
    private String image;
    private Boolean isActive;
    private Date createdAt;
    private Date modifiedAt;

}
