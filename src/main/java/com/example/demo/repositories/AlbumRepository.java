package com.example.demo.repositories;

import com.example.demo.entities.Albums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Albums, Integer> {

}
