package com.example.demo.repositories;

import com.example.demo.entities.FavouriteAlbums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteAlbumRepository extends JpaRepository<FavouriteAlbums, Integer> {
}
