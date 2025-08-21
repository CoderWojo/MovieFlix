package com.movieflix.auth.services;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.exceptions.UserAlreadyExistsException;
import com.movieflix.auth.mappers.AuthMapper;
import com.movieflix.auth.dto.AuthResponse;
import com.movieflix.auth.dto.LoginRequest;
import com.movieflix.auth.dto.RegisterRequest;
import com.movieflix.auth.repositories.UserRepository;

import com.movieflix.exceptions.NotTheSamePasswordException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.time.Duration;

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


    public AuthResponse register(RegisterRequest registerRequest, HttpServletResponse response) {
        var user = authMapper.registerRequestToUser(registerRequest);
        String emailRequest = registerRequest.getEmail();
        String usernameRequest = registerRequest.getUsername();

        if(userRepository.findByEmail(emailRequest).isPresent()) {
            throw new UserAlreadyExistsException("Użytkownik z takim adresem email już istnieje.");
        }
        if(userRepository.findByUsername(usernameRequest).isPresent()) {
            throw new UserAlreadyExistsException("Login zajęty. Zmień go.");
        }

//        sprawdz czy hasla takie same
        if(!registerRequest.getPassword().equals(registerRequest.getRepeat())) {
            throw new NotTheSamePasswordException("Oba hasła muszą być takie same.");
        }
        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateToken(savedUser.getUsername());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(savedUser);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return AuthResponse.builder()
                .accessToken(accessToken)
//                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest, HttpServletResponse response) {
//        authenticationManager.authenticate() automatycznie weryfikuje credentials (czy taki user istnieje w bazie) i haslo encoduje i porównuje.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()
                ) // zauważ że hasło nie szyfrujemy, robi to DaoAuthenticationProvider
        );
        // ^ jeśli username sie zgadza i haslo, to AuthenticationProvider tworzy nowy Token z UserDetails i Manager odbiera ten token i go zapisuję w SecurityContextHolder
        // ^ sprawdza poprawność danych , a poniższa ustawia w kontekscie obj. authentication, wyrzuca AuthenticationException a dokłądniej BadCredentialsException
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        onAuthSuccess(authentication); // logujemy

        String username = ((User) authentication.getPrincipal()).getUsername();    // zwraca User no bo User implementuje UserDetails

//        chyba zbedne, tylko po to aby wyrzucic wyjatek
        User userDb = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username = " + username + " not found."));

        String accessToken = jwtService.generateToken(username);

        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userDb);

//        zapisujemy refreshToken w ciasteczku
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return AuthResponse
                .builder()
                .accessToken(accessToken)
//                .refreshToken(refreshToken.getToken())
                .build();
    }

//    private void onAuthSuccess(Authentication authentication) {
//        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
//        System.out.println("Zalogowany z IP:" + details.getRemoteAddress());
//        System.out.println("SessionId: " + details.getSessionId());
//    }
}
