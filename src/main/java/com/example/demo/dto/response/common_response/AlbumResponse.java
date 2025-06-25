package com.example.demo.dto.response.common_response;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponse {
    private Integer id;
    private String title;
    private String image;
    private Boolean isReleased;
    private Date releaseDate;
    private Integer artistId;
    private Boolean isDeleted;
    private Date createdAt;
    private Date modifiedAt;
    private List<Integer> categoryIds;

}
