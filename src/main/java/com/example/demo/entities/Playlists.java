package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "playlists")
@NoArgsConstructor
@Getter
@Setter
public class Playlists implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "title")
    private String title;

    @Column(name = "is_deleted")
    private Boolean isDeleted;


    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "usersId", referencedColumnName = "id")
    private Users userId; //done

    @OneToMany(mappedBy = "playlistId")
    private Collection<PlaylistSong> playlistSongCollection; //done

    public Playlists(String title, Boolean isDeleted, Date createdAt, Date modifiedAt, Users userId) {
        this.title = title;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.userId = userId;
    }



}
