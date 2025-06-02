package com.example.demo.repositories;

import com.example.demo.entities.Artists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artists, Integer> {

    Optional<Artists> findByIdAndIsDeleted(Integer id, boolean isDeleted);

    @Query("Select COUNT(a) from Artists a where a.isDeleted = :isDeleted")
    int getNumberOfAllNotDeleted(@Param("isDeleted") boolean isDeleted);
}
