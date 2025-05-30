package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
@Entity
@Table(name = "albums")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor 
public class Albums implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "is_released")
    private Boolean isReleased;

    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    @ManyToOne
    private Artists artistId; //done

    @OneToMany(mappedBy = "albumId")
    private Collection<Songs> songsCollection;//done

    @OneToMany(mappedBy = "albumId")
    private Collection<CategoryAlbum> categoryAlbumCollection; //done

    @OneToMany(mappedBy = "albumId")
    private Collection<FavouriteAlbums> favouriteAlbumsCollection; //done




}
