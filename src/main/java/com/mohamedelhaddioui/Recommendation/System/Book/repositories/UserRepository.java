package com.mohamedelhaddioui.Recommendation.System.Book.repositories;

import com.mohamedelhaddioui.Recommendation.System.Book.entites.book;
import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<user,Long> {
    Optional<user> findByGmail(String email);
    @Query("select user from user user where user.nom like :nom ")
    Page<user> searchByUserNom(@Param("nom") String nom, Pageable pageable);
    @Query("select user from user user where user.gmail like :gmail ")
    Page<user> searchByUserGmail(@Param("gmail") String gmail, Pageable pageable);
    @Query("select user from user user where user.prenom like :prenom ")
    Page<user> searchByUserPrenom(@Param("prenom") String prenom, Pageable pageable);
    @Query("select user from user user where user.location like :location ")
    Page<user> searchByUserLocation(@Param("location") String location, Pageable pageable);
}
