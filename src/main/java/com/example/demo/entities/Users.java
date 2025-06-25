package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
    public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false) // tương đương với @Column(nullable = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "user_name")
    private String username;

    @Basic(optional = false)
    @Column(name = "full_name")
    private String fullName;

    @Basic(optional = false)
    @Column(name = "avatar")
    private String avatar;

    @Basic(optional = false)
    @Column(name = "pass_word")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    //ngày tạo người dùng
    @Column(name = "created_at")  //đánh dấu xóa mềm (true = đã xóa)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    //ngày cập nhật cuối
    @Column(name = "modified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @OneToMany(mappedBy = "userId") //done
    private Collection<FavouriteSongs> favouriteSongsCollection;

    @OneToMany(mappedBy = "userId") //done
    private Collection<Playlists> playlistsCollection;

    @OneToMany(mappedBy = "userId") //done
    private Collection<Artists> artistsCollection;

    @OneToMany(mappedBy = "userId") //done
    private Collection<FavouriteAlbums> favouriteAlbumsCollection;

    //Constructor
    public Users(int id, String userName, String fullName, String avatar, String passWord, String phone, String email, String role, Date dob, boolean isDeleted, Date createdAt, Date modifiedAt) {
        this.id = id;
        this.username = userName;
        this.fullName = fullName;
        this.avatar = avatar;
        this.password = passWord;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.dob = dob;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
