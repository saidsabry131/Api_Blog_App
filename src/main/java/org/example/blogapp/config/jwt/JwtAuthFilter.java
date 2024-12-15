package org.example.blogapp.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.blogapp.config.jwt.JwtServices;
import org.example.blogapp.config.security.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtServices jwtServices;

    public JwtAuthFilter(CustomUserDetailsService userDetailsService, JwtServices jwtServices) {
        this.userDetailsService = userDetailsService;
        this.jwtServices = jwtServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {



        // 1- Get token from the request
        String requestToken = request.getHeader("Authorization");
        String username = null;
        String token = null;
        String path=request.getRequestURI();

//        System.out.println("path : "+path);
//        if (path.startsWith("/api/users/") || path.equals("/api/auth/login")) {
//            // Skip JWT validation for permitted paths
//            filterChain.doFilter(request, response);
//            return;
//        }

        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7); // Remove "Bearer " prefix
            try {
                username = jwtServices.getUsernameFromToken(token);
            } catch (Exception e) {
                System.out.println("Error extracting username from token: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid token format or token is null");
        }

        // 2- Validate token and set SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtServices.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Invalid JWT token");
            }
        } else {
            System.out.println("Username is null or authentication already exists");
        }

        // Ensure filter chain is executed
        filterChain.doFilter(request, response);
    }
}
