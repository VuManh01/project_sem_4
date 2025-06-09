package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "category_album")
@NoArgsConstructor
@Getter
@Setter

public class CategoryAlbum implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "album_id", referencedColumnName = "id")
    @ManyToOne
    private Albums albumId; // done

    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    private Categories categoryId;

    public CategoryAlbum(Albums albumId, Categories categoryId) {
        this.albumId = albumId;
        this.categoryId = categoryId;
    }
}
