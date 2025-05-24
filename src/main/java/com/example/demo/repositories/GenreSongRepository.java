package com.example.demo.repositories;

import com.example.demo.entities.GenreSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreSongRepository extends JpaRepository<GenreSong, Integer> {
}
