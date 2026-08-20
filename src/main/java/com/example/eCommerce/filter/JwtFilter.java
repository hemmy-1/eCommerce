package com.example.eCommerce.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.eCommerce.service.JwtService;
import com.example.eCommerce.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserService userService;

    public JwtFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {

        System.out.println("Gets to the filter");
        System.out.println("Gets to the filter");
        System.out.println("Gets to the filter");

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Check if Authorization header contains a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract token and username
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // 3. Authenticate if token is valid and user is not yet authenticated in
        // context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            System.out.println("is Valid Token" + jwtService.isValidToken(jwt, userDetails));

            if (jwtService.isValidToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Set authentication in context
                System.out.println(authToken);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                ;
            }
        }

        // 4. Pass request down the chain
        filterChain.doFilter(request, response);
    }
}
