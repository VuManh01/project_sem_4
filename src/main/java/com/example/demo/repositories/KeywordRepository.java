package com.example.demo.repositories;

import com.example.demo.entities.Keywords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keywords, Integer> {

    @Query("Select COUNT(a) from Keywords a")
    int getNumberOfAll();
}
