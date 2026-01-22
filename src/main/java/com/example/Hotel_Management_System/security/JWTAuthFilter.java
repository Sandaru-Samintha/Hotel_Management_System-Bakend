package com.example.Hotel_Management_System.security;

import com.example.Hotel_Management_System.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWTAuthFilter
 *
 * This filter runs once for every HTTP request.
 * It intercepts incoming requests, extracts the JWT token from the
 * Authorization header, validates it, and sets the authenticated user
 * into Spring Security's SecurityContext.
 */
@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    // Utility class for extracting and validating JWT tokens
    @Autowired
    private JWTUtils jwtUtils;

    // Loads user details from cache or database
    @Autowired
    private CachingUserDetailsService cachingUserDetailsService;

    /**
     * This method is executed for every incoming HTTP request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Get the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");

        final String jwtToken;
        final String userEmail;

        // If Authorization header is missing or empty,
        // continue the filter chain without authentication
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token (skipping "Bearer ")
        jwtToken = authHeader.substring(7);

        // Extract username/email from JWT token
        userEmail = jwtUtils.extractUsername(jwtToken);

        // Check:
        // 1. Username exists in token
        // 2. User is not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from database or cache
            UserDetails userDetails =
                    cachingUserDetailsService.loadUserByUsername(userEmail);

            // Validate JWT token against user details
            if (jwtUtils.isValidToken(jwtToken, userDetails)) {

                // Create an empty security context
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // Create authentication token with user authorities
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Attach request-specific details (IP, session, etc.)
                token.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set authentication into security context
                securityContext.setAuthentication(token);

                // Set the context in Spring Security
                SecurityContextHolder.setContext(securityContext);
            }
        }

        // Continue request processing
        filterChain.doFilter(request, response);
    }
}
