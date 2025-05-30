package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrUpdateGenreSong {
    private Integer id;
    @NotNull(message = "genreId is required")
    private Integer genreId;
    @NotNull(message = "songId is required")
    private Integer songId;
}
