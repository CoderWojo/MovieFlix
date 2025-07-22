package com.movieflix.auth.repositories;

import com.movieflix.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

//    W JPQL nie używamy nazw tabel tylko nazw encji
    @Query("update User u SET u.password = :password WHERE u.username = :username")
    @Modifying  // Spring Data przy zapytaniu UPDATE zwraca liczbę zmodyfikowanych wierszy
    @Transactional
    int updateOtp(@Param("username") String username, @Param("password") String newPassword);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
