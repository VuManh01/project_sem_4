package com.example.demo.repositories;

import com.example.demo.entities.GenreSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreSongRepository extends JpaRepository<GenreSong, Integer> {

    @Query("SELECT gs FROM GenreSong gs WHERE gs.songId.id = :songId AND gs.genreId.isDeleted = :isDeleted")
    List<GenreSong> findBySongId(@Param("songId") Integer songId, @Param("isDeleted") boolean isDeleted);

    @Query("SELECT gs FROM GenreSong gs WHERE gs.genreId.id = :genreId AND gs.songId.id = :songId")
    Optional<GenreSong> findByGenreIdAndSongId(@Param("genreId") Integer genreId, @Param("songId") Integer songId);

    @Query("SELECT gs FROM GenreSong gs WHERE gs.genreId.id = :genreId AND gs.songId.isDeleted = :isDeleted")
    List<GenreSong> findByGenreId(@Param("genreId") Integer genreId, @Param("isDeleted") boolean isDeleted);

}
