package com.example.hotelManagmentSystem.config;


import com.example.hotelManagmentSystem.core.exceptions.InvalidTokenException;
import com.example.hotelManagmentSystem.dataproviders.entity.Token;
import com.example.hotelManagmentSystem.dataproviders.repository.TokenRepository;
import com.example.hotelManagmentSystem.dataproviders.service.implementations.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenRepository tokenRepository;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        logger.info(request.getServletPath());
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        jwtToken = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwtToken);
        }catch (ExpiredJwtException e){
            throw new InvalidTokenException("Token expired or invalid");
        }
        //check if is authenticated using securityholder
        if (userEmail != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
           var isTokenValid = tokenRepository.findByToken(jwtToken)
                   .map(token1 -> !token1.getExpired() && !token1.getRevoked())
                   .orElse(false);
           if (!isTokenValid){
               throw new InvalidTokenException("Token revoked!");
           }
            if (jwtService.isTokenValid(jwtToken,userDetails)
           ){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,null
                                ,userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request,response);
        }

    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        logger.info("Should Not Filter");
//       return request.getServletPath().contains("/v3/api-docs/**") ||
//               request.getServletPath().contains("/swagger-ui/**") || request.getServletPath().contains("/swagger-ui.html");
//    }
}}

