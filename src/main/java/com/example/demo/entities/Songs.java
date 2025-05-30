package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import java.util.Collection;

@Entity
@Table(name = "songs")
@NoArgsConstructor
@Getter
@Setter
public class Songs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "title")
    private String title;

    @Column(name = "audio_path")
    private String audioPath;

    @Column(name = "listen_amount")
    private Integer listenAmount;

    @Column(name = "feature_artist")
    private String featureArtist;

    @Column(name = "lyric_file_path")
    private String lyricFilePath;

    @Column(name = "is_pending")
    private Boolean isPending;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
//=====================================================================
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    @ManyToOne
    private Albums albumId; //done

    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    @ManyToOne
    private Artists artistId; //done
//=====================================================================
    @OneToMany(mappedBy = "songId")
    private Collection<FavouriteSongs> favouriteSongsCollection; //done

    @OneToMany(mappedBy = "songId")
    private Collection<PlaylistSong> playlistSongCollection; //done

    @OneToMany(mappedBy = "songId")
    private Collection<GenreSong> genreSongCollection; //done

    @OneToMany(mappedBy = "songId")
    private Collection<ViewInMonth> likeAndViewInMonthCollection; //done

//    public Songs(Integer id, String title, String audioPath, Integer listenAmount, String featureArtist, String lyricFilePath, Boolean isPending, Boolean isDeleted, Date createdAt, Date modifiedAt, Albums albumId, Artists artistId, Collection<FavouriteSongs> favouriteSongsCollection, Collection<PlaylistSong> playlistSongCollection, Collection<GenreSong> genreSongCollection, Collection<ViewInMonth> likeAndViewInMonthCollection) {
//        this.id = id;
//        this.title = title;
//        this.audioPath = audioPath;
//        this.listenAmount = listenAmount;
//        this.featureArtist = featureArtist;
//        this.lyricFilePath = lyricFilePath;
//        this.isPending = isPending;
//        this.isDeleted = isDeleted;
//        this.createdAt = createdAt;
//        this.modifiedAt = modifiedAt;
//        this.albumId = albumId;
//        this.artistId = artistId;
//        this.favouriteSongsCollection = favouriteSongsCollection;
//        this.playlistSongCollection = playlistSongCollection;
//        this.genreSongCollection = genreSongCollection;
//        this.likeAndViewInMonthCollection = likeAndViewInMonthCollection;
//    }

    public Songs(String title, String audioPath, Integer listenAmount, String featureArtist, String lyricFilePath, Boolean isPending, Boolean isDeleted, Date createdAt, Date modifiedAt, Artists artistId) {
        this.title = title;
        this.audioPath = audioPath;
        this.listenAmount = listenAmount;
        this.featureArtist = featureArtist;
        this.lyricFilePath = lyricFilePath;
        this.isPending = isPending;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.artistId = artistId;
    }
}
