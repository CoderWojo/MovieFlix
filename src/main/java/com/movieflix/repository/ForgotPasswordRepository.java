package com.movieflix.repository;

import com.movieflix.auth.entities.User;
import com.movieflix.entities.ForgotPassword;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("update ForgotPassword fp set fp.attempts = :attempts where fp.user.id = :id")
    @Transactional
    @Modifying
    int updateAttemptsByUserId(@Param("attempts") int attempts, @Param("id") int id);

    @Query("update ForgotPassword fp set fp.used = :used where fp.user.id = :id")
    @Modifying
    @Transactional
    int updateUsedByUserId(@Param("used") boolean used, int id);

    @Query("update ForgotPassword fp set fp.verifiedAt = :verifiedAt where fp.user.id = :id")
    @Modifying
    @Transactional
    int updateVerifiedAtByUserId(@Param("verifiedAt") LocalDateTime now, @Param("id") Integer id);

    Optional<ForgotPassword> findByUserAndOtp(User u, Integer code);

    Optional<ForgotPassword> findByUser(User user);
}