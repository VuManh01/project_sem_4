package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Collection;

@Entity
@Table(name = "genres")
@NoArgsConstructor
@Getter
@Setter
public class Genres implements Serializable {
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

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @OneToMany(mappedBy = "genreId")
    private Collection<GenreSong> genreSongCollection; //done

    @JoinColumn(name = "color_id", referencedColumnName = "id")
    @ManyToOne
    private Colors colorId; //done

    public Genres(Integer id, String title, String image, Boolean isDeleted, Date createdAt, Date modifiedAt, Collection<GenreSong> genreSongCollection, Colors colorId) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.genreSongCollection = genreSongCollection;
        this.colorId = colorId;
    }
}
