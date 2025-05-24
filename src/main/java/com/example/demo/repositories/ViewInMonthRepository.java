package com.example.demo.repositories;

import com.example.demo.entities.ViewInMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewInMonthRepository extends JpaRepository<ViewInMonth, Integer> {
}
