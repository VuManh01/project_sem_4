package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "view_in_moths")
@NoArgsConstructor
@Getter
@Setter
public class ViewInMonth implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "listen_amount")
    private Integer listenAmount;

    @JoinColumn(name = "month_id", referencedColumnName = "id")
    @ManyToOne
    private MonthOfYear monthId; //done

    @JoinColumn(name = "song_id", referencedColumnName = "id")
    @ManyToOne
    private Songs songId; //done

    public ViewInMonth(Integer id, Integer listenAmount, MonthOfYear monthId, Songs songId) {
        this.id = id;
        this.listenAmount = listenAmount;
        this.monthId = monthId;
        this.songId = songId;
    }
}
