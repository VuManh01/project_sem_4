package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrUpdateArtist {
    private Integer id;
    @NotBlank(message = "artistName is required")
    private String artistName;
    private String image;
    @NotBlank(message = "bio is required")
    private String bio;
    private Integer userId;
}
