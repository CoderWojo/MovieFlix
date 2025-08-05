package com.movieflix.auth.repositories;

import com.movieflix.auth.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.password = :password WHERE u.username = :username")
    int updatePasswordByOwn(@Param("username") String username, @Param("password") String newPassword);

//    W JPQL nie używamy nazw tabel tylko nazw encji
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    @Modifying  // Spring Data przy zapytaniu UPDATE zwraca liczbę zmodyfikowanych wierszy
    @Transactional
    int updatePassword(@Param("email") String email, @Param("password") String newPassword);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
