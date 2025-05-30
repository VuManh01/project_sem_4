package com.example.demo.repositories;

import com.example.demo.entities.Songs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Songs, Integer> {

    @Query("Select a from Songs a where a.isDeleted = :isDeleted")
    List<Songs> findAllNotDeleted(boolean isDeleted);

    Optional<Songs> findByTitle(String title);

    Optional<Songs> findByIdAndIsDeleted(Integer id, boolean isDeleted);
}
