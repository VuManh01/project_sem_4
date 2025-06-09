package com.example.demo.repositories;

import com.example.demo.entities.FavouriteSongs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteSongRepository extends JpaRepository<FavouriteSongs, Integer>
{
    @Query("SELECT fs FROM FavouriteSongs fs WHERE fs.songId.id = :songId AND fs.songId.isDeleted = :isDeleted")
    List<FavouriteSongs> findFSBySongId(@Param("songId") Integer songId, @Param("isDeleted") boolean isDeleted);

    @Query("SELECT fs FROM FavouriteSongs fs WHERE fs.userId.id = :userId AND fs.songId.isDeleted = :isDeleted")
    List<FavouriteSongs> findFSByUserIdPaging(@Param("userId") Integer userId, @Param("isDeleted") boolean isDeleted, Pageable pageable);

}
