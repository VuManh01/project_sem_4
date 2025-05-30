package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "genre_song")
@NoArgsConstructor
@Setter
@Getter
public class GenreSong implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    @ManyToOne
    private Genres genreId; //done

    @JoinColumn(name = "song_id", referencedColumnName = "id")
    @ManyToOne
    private Songs songId; //done

    public GenreSong( Genres genreId, Songs songId) {
        this.genreId = genreId;
        this.songId = songId;
    }


}
