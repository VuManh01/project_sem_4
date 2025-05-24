package com.example.demo.repositories;

import com.example.demo.entities.Playlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlists, Integer> {
}
