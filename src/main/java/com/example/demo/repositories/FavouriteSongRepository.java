package com.example.demo.repositories;

import com.example.demo.entities.FavouriteSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteSongRepository extends JpaRepository<FavouriteSongs, Integer>
{
}
