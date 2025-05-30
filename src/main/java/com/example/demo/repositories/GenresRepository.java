package com.example.demo.repositories;

import com.example.demo.entities.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenresRepository extends JpaRepository<Genres, Integer> {

    Optional<Genres> findByIdAndIsDeleted(Integer id, boolean isDeleted);


}
