package com.movieflix.repository;

import com.movieflix.auth.entities.User;
import com.movieflix.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    Optional<ForgotPassword> findByUserAndOtp(User u, Integer code);

    Optional<ForgotPassword> findByUser(User user);
}