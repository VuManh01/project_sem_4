package com.example.demo.repositories;

import com.example.demo.entities.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Colors, Integer> {
}
