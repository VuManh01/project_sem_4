package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "artists")
@NoArgsConstructor
@Getter
@Setter
public class Artists implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "image")
    private String image;

    @Lob
    @Column(name = "bio")
    private String bio;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users userId; //done

    @OneToMany(mappedBy = "artistId")
    private Collection<Songs> songsCollection; //done

    @OneToMany(mappedBy = "artistId")
    private Collection<Albums> albumsId; //done

    public Artists(String artistName, String image, String bio, Boolean isDeleted, Date createdAt, Date modifiedAt) {
        this.artistName = artistName;
        this.image = image;
        this.bio = bio;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
