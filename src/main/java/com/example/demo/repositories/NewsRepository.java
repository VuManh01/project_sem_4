package com.example.demo.repositories;

import com.example.demo.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query("Select COUNT(a) from News a")
    int getNumberOfAll();
}
