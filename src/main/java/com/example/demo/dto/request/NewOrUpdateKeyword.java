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
public class NewOrUpdateKeyword {
    private Integer id;
    @NotBlank(message = "content is required")
    private String content;
    @NotNull(message = "isActive is required")
    private Boolean isActive;

}
