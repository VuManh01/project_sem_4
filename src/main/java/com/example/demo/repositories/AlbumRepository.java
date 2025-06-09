package com.example.demo.repositories;

import com.example.demo.entities.Albums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Albums, Integer> {

    Optional<Albums> findByIdAndIsDeleted(Integer id, boolean isDeleted);

    @Query("Select COUNT(a) from Albums a where a.isDeleted = :isDeleted")
    int getNumberOfAllNotDeleted(@Param("isDeleted") boolean isDeleted);

    @Query("Select a from Albums a where a.isDeleted = :isDeleted")
    List<Albums> findAllNotDeletedPaging(boolean isDeleted, Pageable pageable);

    Optional<Albums> findByTitle(String title);

    @Query("Select a from Albums a where a.artistId.id = :arId AND a.isDeleted = :isDeleted")
    List<Albums> findAllByArtistIdPaging(@Param("arId") Integer artistId, @Param("isDeleted") boolean isDeleted, Pageable pageable);
}
