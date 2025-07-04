package com.example.demo.repositories;

import com.example.demo.entities.Songs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Songs, Integer> {

    @Query("Select a from Songs a where a.isDeleted = :isDeleted")
    List<Songs> findAllNotDeleted(boolean isDeleted);

    Optional<Songs> findByTitle(String title);

    Optional<Songs> findByIdAndIsDeleted(Integer id, boolean isDeleted);

    @Query("Select a from Songs a where a.isDeleted = :isDeleted")
    List<Songs> findAllNotDeletedPaging(boolean isDeleted, Pageable pageable);

    @Query("Select COUNT(a) from Songs a where a.isDeleted = :isDeleted")
    int getNumberOfAllNotDeleted(@Param("isDeleted") boolean isDeleted);

    @Query("Select a from Songs a where a.albumId.id = :alId AND a.isDeleted = :isDeleted")
    List<Songs> findAllByAlbumIdPaging(@Param("alId") Integer albumId, @Param("isDeleted") boolean isDeleted, Pageable pageable);

    @Query("Select a from Songs a where a.artistId.id = :arId AND a.isDeleted = :isDeleted")
    List<Songs> findAllByArtistIdForAdmin(@Param("arId") Integer artistId, @Param("isDeleted") boolean isDeleted, Pageable pageable);

    @Query("Select a from Songs a where a.albumId.id = :alId AND a.isDeleted = :isDeleted AND a.isPending = :isPending")
    List<Songs> findAllByAlbumId(@Param("alId") Integer albumId, @Param("isDeleted") boolean isDeleted, @Param("isPending") boolean isPending);
}
