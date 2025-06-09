package com.example.demo.repositories;

import com.example.demo.entities.CategoryAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryAlbumRepository extends JpaRepository<CategoryAlbum, Integer> {
    @Query("SELECT sa FROM CategoryAlbum sa WHERE sa.categoryId.id = :categoryId AND sa.albumId.id = :albumId")
    Optional<CategoryAlbum> findByCategoryIdAndAlbumId(@Param("categoryId") Integer categoryId, @Param("albumId") Integer albumId);
}
