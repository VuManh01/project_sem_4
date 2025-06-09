package com.example.demo.repositories;

import com.example.demo.entities.FavouriteAlbums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteAlbumRepository extends JpaRepository<FavouriteAlbums, Integer> {
    @Query("SELECT fa FROM FavouriteAlbums fa WHERE fa.albumId.id = :albumId AND fa.albumId.isDeleted = :isDeleted")
    List<FavouriteAlbums> findFAByAlbumId(@Param("albumId") Integer albumId, @Param("isDeleted") boolean isDeleted);

    @Query("SELECT fa FROM FavouriteAlbums fa WHERE fa.userId.id = :userId AND fa.albumId.isDeleted = :isDeleted")
    List<FavouriteAlbums> findFAByUserIdPaging(@Param("userId") Integer userId, @Param("isDeleted") boolean isDeleted, Pageable pageable);
}
