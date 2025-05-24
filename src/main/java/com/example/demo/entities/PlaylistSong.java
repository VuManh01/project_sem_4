package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "play_list_song")
@NoArgsConstructor
@Getter
@Setter
public class PlaylistSong implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "playlist_id", referencedColumnName = "id")
    @ManyToOne
    private Playlists playlistId; // done

    @JoinColumn(name = "song_id", referencedColumnName = "id")
    @ManyToOne
    private Songs songId; //done

    public PlaylistSong(Integer id, Playlists playlistId, Songs songId) {
        this.id = id;
        this.playlistId = playlistId;
        this.songId = songId;
    }
}
