package com.example.demo.repositories;

import com.example.demo.entities.Playlists;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlists, Integer> {
    @Query("Select COUNT(a) from Playlists a where a.isDeleted = :isDeleted")
    int getNumberOfAllNotDeleted(@Param("isDeleted") boolean isDeleted);

    @Query("Select a from Playlists a where a.isDeleted = :isDeleted")
    List<Playlists> findAllNotDeletedPaging(boolean isDeleted, Pageable pageable);

    Optional<Playlists> findByIdAndIsDeleted(Integer id, boolean isDeleted);

    @Query("Select a from Playlists a where a.title Like %:searchTxt% AND a.isDeleted = :isDeleted")
    List<Playlists> searchNotDeletedPaging(@Param("searchTxt") String searchTxt, boolean isDeleted, Pageable pageable);

}
