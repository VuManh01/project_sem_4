package com.example.demo.repositories;

import com.example.demo.entities.Keywords;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keywords, Integer> {

    @Query("Select COUNT(a) from Keywords a")
    int getNumberOfAll();

    @Query("Select a from Keywords a")
    List<Keywords> findAllPaging(Pageable pageable);

    Optional<Keywords> findByContent(String content);
}
