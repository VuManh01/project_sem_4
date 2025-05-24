package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "colors")
@NoArgsConstructor
@Getter
@Setter
public class Colors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "colorId")
    private Collection<Genres> genresCollection;

    public Colors(Integer id, String title, Collection<Genres> genresCollection) {
        this.id = id;
        this.title = title;
        this.genresCollection = genresCollection;
    }
}
