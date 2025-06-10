package com.example.demo.repositories;

import com.example.demo.entities.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query("Select COUNT(a) from News a")
    int getNumberOfAll();

    @Query("Select a from News a")
    List<News> findAllPaging(Pageable pageable);

    Optional<News> findByTitle(String title);

}
