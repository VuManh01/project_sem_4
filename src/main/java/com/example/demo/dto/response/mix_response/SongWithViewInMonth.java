package com.example.demo.dto.response.mix_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SongWithViewInMonth {
    private int songId;
    private String songName;
    private String artistName;
    private String albumName;
    private int listenInMonth;
}
