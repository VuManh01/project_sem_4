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
public class PlaylistDisplayForAdmin implements Serializable {
    private Integer id;
    private String title;
    private Boolean isDeleted;
    private Date createdAt;
    private Date modifiedAt;
    private String username;
    private Integer totalSong;//field thiếu
}
