package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrUpdatePlaylist {
    private Integer id;
    @NotBlank(message = "title is required")
    private String title;
    @NotNull(message = "userId is required")
    private Integer userId;
}
