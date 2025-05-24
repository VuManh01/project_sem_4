package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "favourite_albums")
@NoArgsConstructor
@Getter
@Setter
public class FavouriteAlbums implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usersId", referencedColumnName = "id")
    private Users userId; //done

    @ManyToOne
    @JoinColumn(name= "album_id", referencedColumnName = "id")
    private Albums albumId; //done


}
