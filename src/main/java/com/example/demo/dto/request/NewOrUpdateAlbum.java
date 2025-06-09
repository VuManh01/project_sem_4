package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NewOrUpdateAlbum {
    private Integer id;
    @NotBlank(message = "title is required")
    private String title;
    private String image;
    @NotNull(message = "releaseDate is required")
    private Date releaseDate;
    @NotNull(message = "artistId is required")
    private Integer artistId;
    @NotNull(message = "cateIds list is required")
    private List<Integer> cateIds;

}
