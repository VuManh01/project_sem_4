package com.example.demo.repositories;

import com.example.demo.entities.Genres;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenresRepository extends JpaRepository<Genres, Integer> {

    Optional<Genres> findByIdAndIsDeleted(Integer id, boolean isDeleted);

    Optional<Genres> findByTitle(String title);

    @Query("Select a from Genres a where a.isDeleted = :isDeleted")
    List<Genres> findAllNotDeletedPaging(boolean isDeleted, Pageable pageable);

    @Query("Select COUNT(a) from Genres a where a.isDeleted = :isDeleted")
    int getNumberOfAllNotDeleted(@Param("isDeleted") boolean isDeleted);
}
