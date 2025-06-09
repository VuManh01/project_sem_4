package com.example.demo.repositories;

import com.example.demo.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {

    @Query("Select COUNT(a) from Categories a where a.isDeleted = :isDeleted")
    int getNumberOfAllNotDeleted(@Param("isDeleted") boolean isDeleted);

    Optional<Categories> findByIdAndIsDeleted(Integer id, boolean isDeleted);

}
