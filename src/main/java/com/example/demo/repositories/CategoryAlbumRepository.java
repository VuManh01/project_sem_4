package com.example.demo.repositories;

import com.example.demo.entities.CategoryAlbum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryAlbumRepository extends JpaRepository<CategoryAlbum, Integer> {
    @Query("SELECT sa FROM CategoryAlbum sa WHERE sa.categoryId.id = :categoryId AND sa.albumId.id = :albumId")
    Optional<CategoryAlbum> findByCategoryIdAndAlbumId(@Param("categoryId") Integer categoryId, @Param("albumId") Integer albumId);

    @Query("SELECT sa FROM CategoryAlbum sa WHERE sa.categoryId.id = :categoryId AND sa.albumId.isDeleted = :isDeleted")
    List<CategoryAlbum> findAllByCategoryId(@Param("categoryId") Integer categoryId, @Param("isDeleted") boolean isDeleted);

    @Query("SELECT sa FROM CategoryAlbum sa WHERE sa.categoryId.id = :categoryId AND sa.albumId.isDeleted = :isDeleted")
    List<CategoryAlbum> findAllByCategoryIdPaging(@Param("categoryId") Integer categoryId, @Param("isDeleted") boolean isDeleted, Pageable pageable);

    @Query("SELECT sa FROM CategoryAlbum sa WHERE sa.albumId.id = :albumId AND sa.categoryId.isDeleted = :isDeleted")
    List<CategoryAlbum> findAllByAlbumId(@Param("albumId") Integer albumId, @Param("isDeleted") boolean isDeleted);
}
