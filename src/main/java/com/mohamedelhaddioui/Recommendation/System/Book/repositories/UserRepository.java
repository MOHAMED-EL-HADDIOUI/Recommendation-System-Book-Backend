package com.mohamedelhaddioui.Recommendation.System.Book.repositories;

import com.mohamedelhaddioui.Recommendation.System.Book.entites.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<user,Long> {
    Optional<user> findByGmail(String email);
}
