package com.movieflix.auth.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;


@Service
public class AuthFilterService extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public AuthFilterService(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt;

        System.out.println(Collections.list(request.getHeaderNames()));
        System.out.println("Jwt odczytane: " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        jwt = authHeader.substring(7);

        String username;
        System.out.println("JESTEM TU");
        try {
            username = jwtService.extractUsername(jwt); // throws ExpiredJwtException
        } catch(ExpiredJwtException ex) {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            String json = getString(request, ex);
            response.getWriter()
                   .write(json);

           return;  // nie przekazuj dalej, tak robimy gdy coś złego się stało bo nie chcemy aby inne filtry a później kontrolery obsugiwały to żądanie , może się to wiązać z próbą nadpisania odpowiedzi(IllegalStateException: Cannot call sendError() after the response has been commited)
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            System.out.println("User o takim jwt istnieje");
            System.out.println("To on: " + userDetails.getAuthorities());
            if (jwtService.isTokenValid(jwt, userDetails)) {
//  UsernamePasswordAuthenticationToken konstruktor przyjmuje userDetails obiekt (nasz User)
                System.out.println("TOken jest prawidlowy");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("User authenticated");
            }
        }
        filterChain.doFilter(request, response);
    }

//    oznaczamy static bo nie używa 'this.' - korzysta tylko z danych które otrzymała jako parametry i swoich
    private static String getString(HttpServletRequest request, ExpiredJwtException ex) {
        String timestamp = LocalDateTime.now().toString();
        Integer status = HttpStatus.BAD_REQUEST.value();
        String exception = ex.getClass().getSimpleName();
        String message = ex.getMessage();
        String path = request.getRequestURI();
        return """
               {
                "timestamp": "%s",
                "status": "%d",
                "exception": "%s",
                "message": "%s",
                "path": "%s"
               }
               """.formatted(timestamp, status, exception, message, path);
    }
}
