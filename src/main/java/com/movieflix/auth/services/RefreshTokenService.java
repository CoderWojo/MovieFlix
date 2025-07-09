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
    private final long refreshTokenValidityMillis = 1000 * 60 * 60 * 24 * 7; // 7 dni

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(token)
                .expirationTime(Instant.now().plus(Duration.ofMillis(refreshTokenValidityMillis)))
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    @Transactional
    public boolean verifyRefreshToken(String token) {
        RefreshToken refToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(token));
        if (refToken.getExpirationTime().compareTo(Instant.now())< 0) {
            // out of date
            refreshTokenRepository.delete(refToken);
            throw new RefreshTokenOutOfDateException(token);
        }

        return true;
    }

    // TODO: stwórz metodę która usuwa z bazy przestarzałe tokeny
}
