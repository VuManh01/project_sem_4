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
public class NewOrUpdateCategoryAlbum {

    private Integer id;
    @NotNull(message = "albumId is required")
    private Integer albumId;
    @NotNull(message = "categoryId is required")
    private Integer categoryId;
}
