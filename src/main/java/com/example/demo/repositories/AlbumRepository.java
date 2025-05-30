package com.example.demo.repositories;

import com.example.demo.entities.Albums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Albums, Integer> {

    Optional<Albums> findByIdAndIsDeleted(Integer id, boolean isDeleted);

}
