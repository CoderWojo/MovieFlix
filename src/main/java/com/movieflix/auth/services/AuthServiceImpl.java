package com.movieflix.auth.services;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.exceptions.UserAlreadyExistsException;
import com.movieflix.auth.mappers.AuthMapper;
import com.movieflix.auth.model.AuthResponse;
import com.movieflix.auth.model.LoginRequest;
import com.movieflix.auth.model.RegisterRequest;
import com.movieflix.auth.repositories.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(AuthMapper authMapper, UserRepository userRepository, JwtService jwtService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.authMapper = authMapper;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest registerRequest) {
//        sprawdzmy czy email juz nie jest uzyty

        var user = authMapper.registerRequestToUser(registerRequest);
        String emailRequest = user.getEmail();
        String usernameRequest = registerRequest.getUsername();
        if(userRepository.findByEmail(emailRequest).isPresent()) {
            throw new UserAlreadyExistsException("User with given email already exists! Please change.");
        }
        if (userRepository.findByUsername(usernameRequest).isPresent()) {
            throw new UserAlreadyExistsException("User with given username already exists! Please change.");
        }

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateToken(savedUser.getUsername());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
//        authenticationManager.authenticate() automatycznie weryfikuje credentials (czy taki user istnieje w bazie) i haslo encoduje i porównuje.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        // ^ sprawdza poprawność danych , a poniższa ustawia w kontekscie obj. authentication, wyrzuca AuthenticationException a dokłądniej BadCredentialsException
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = ((User) authentication.getPrincipal()).getUsername();    // zwraca User no bo User implementuje UserDetails

//        chyba zbedne, tylko po to aby wyrzucic wyjatek
        User userDb = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username = " + username + " not found."));

        String accessToken = jwtService.generateToken(username);

        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userDb);

        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
