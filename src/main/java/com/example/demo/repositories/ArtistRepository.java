package com.example.demo.repositories;

import com.example.demo.entities.Artists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artists, Integer> {
}
