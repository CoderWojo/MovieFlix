package com.movieflix.exception;

import java.time.LocalDateTime;

public record ApiError(
//        autom. private final fields
//        public AllArgsConstructor
//        getters, equals, hashCode, toString
        LocalDateTime timestamp,
        String status,
        String exception,
        String message,
        String path
) {}
/*
w bloku {} można dodać dodatkowe metody statyczne/instancyjne, statyczne stałe albo compact constructor
     public record User(String email, int age) {
        public User {
            if (age < 0) throw new IllegalArgumentException("age must be ≥ 0");
        }
    }

 */