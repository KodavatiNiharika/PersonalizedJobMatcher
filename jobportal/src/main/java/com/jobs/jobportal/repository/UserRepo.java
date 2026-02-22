package com.jobs.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.jobs.jobportal.model.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> { 
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);

    @Query("SELECT u.atsThreshold FROM User u WHERE u.id = :id")
    Integer getAtsThreshold(@Param("id") Long id);
}