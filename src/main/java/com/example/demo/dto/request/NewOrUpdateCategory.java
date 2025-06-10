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
public class NewOrUpdateCategory {
    private Integer id;
    @NotBlank(message = "title is required")
    private String title;
    private String description;
}
