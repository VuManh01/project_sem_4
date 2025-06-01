package com.example.demo.repositories;

import com.example.demo.entities.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    List<Users> findAllByIsDeleted(boolean isDeleted);

    Optional<Users> findByIdAndIsDeleted(Integer id, boolean isDeleted);

    @Query("Select a from Users a where a.isDeleted = :isDeleted")
    List<Users> findAllNotDeletedPaging(boolean isDeleted, Pageable pageable);


    Optional<Users> findByUsername(String username);

    Optional<Users> findByPhone(String phone);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsernameAndIsDeleted(String username, boolean isDeleted);

    @Query("SELECT a FROM Users a WHERE a.username LiKE %:searchTxt% AND a.isDeleted = :isDeleted")
    List<Users> searchNotDeletedPaging(@Param("searchTxt") String searchTxt, boolean isDeleted, Pageable pageable);
}
