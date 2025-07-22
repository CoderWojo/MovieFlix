package com.movieflix.auth.services;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.exceptions.RefreshTokenNotFoundException;
import com.movieflix.auth.exceptions.RefreshTokenOutOfDateException;
import com.movieflix.auth.repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

//    TODO: dorozum ten refreshtoken
    public RefreshToken generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        // 7 dni1000 * 60 * 60 * 24 * 7
        long refreshTokenValidityMillis = 1000 * 60 * 30;//30min
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(token)
                .expirationTime(Instant.now().plus(Duration.ofMillis(refreshTokenValidityMillis)))
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(token));
        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            // out of date
            refreshTokenRepository.delete(refToken);
            throw new RefreshTokenOutOfDateException();
        }
        // token is good, wysylamy nowy, kasujemy stary


        return refToken;
    }

    // TODO: stwórz metodę która usuwa z bazy przestarzałe tokeny
}
