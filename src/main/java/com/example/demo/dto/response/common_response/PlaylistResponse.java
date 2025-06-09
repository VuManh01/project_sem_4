package com.example.demo.dto.response.common_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponse {
    private Integer id;
    private String title;
    private Boolean isDeleted;
    private Date createdAt;
    private Date modifiedAt;
    private Integer userId;

}
