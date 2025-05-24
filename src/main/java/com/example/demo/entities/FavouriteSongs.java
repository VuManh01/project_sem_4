package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "favourite_songs")
@NoArgsConstructor
@Getter
@Setter
public class FavouriteSongs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "song_id", referencedColumnName = "id")
    @ManyToOne
    private Songs songId; // dont

    @JoinColumn(name = "usersId", referencedColumnName = "id")
    @ManyToOne
    private Users userId; // done

    public FavouriteSongs(Integer id, Songs songId, Users userId) {
        this.id = id;
        this.songId = songId;
        this.userId = userId;
    }
}
