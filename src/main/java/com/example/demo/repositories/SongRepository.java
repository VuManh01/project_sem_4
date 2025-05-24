package com.example.demo.repositories;

import com.example.demo.entities.Songs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Songs, Integer> {
}
