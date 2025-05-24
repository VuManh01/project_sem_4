package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "month_of_years")
@NoArgsConstructor
@Getter
@Setter
public class MonthOfYear implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "monthId")
    private Collection<ViewInMonth> likeAndViewInMonthCollection; //done

    public MonthOfYear(Integer id, String title, Collection<ViewInMonth> likeAndViewInMonthCollection) {
        this.id = id;
        this.title = title;
        this.likeAndViewInMonthCollection = likeAndViewInMonthCollection;
    }
}
