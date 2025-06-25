package com.example.demo.dto.response.display_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SongDisplay implements Serializable {


    private Integer id;
    private String title;
    private String audioPath;
    private String lyricFilePath;
    private String featureArtist;
    private Boolean isPending;
    private Boolean isDeleted;
    private Date createdAt;
    private Date modifiedAt;
    private String albumTitle;
    private String albumImage;
    private String artistName;
    private List<String> genreNames;
}
