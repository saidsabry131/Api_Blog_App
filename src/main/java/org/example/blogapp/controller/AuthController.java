package org.example.blogapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.blogapp.config.jwt.JwtRequest;
import org.example.blogapp.config.jwt.JwtResponse;
import org.example.blogapp.config.jwt.JwtServices;
import org.example.blogapp.config.security.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    private final JwtServices jwtServices;
    private final CustomUserDetailsService userDetailsService;

    private final AuthenticationManager manager;

    public AuthController(JwtServices jwtServices, CustomUserDetailsService userDetailsService, AuthenticationManager manager) {
        this.jwtServices = jwtServices;
        this.userDetailsService = userDetailsService;
        this.manager = manager;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createToken(@RequestBody JwtRequest jwtRequest, HttpServletRequest request)
    {
        manager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));

        UserDetails userPrincipal=userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        String token =jwtServices.generateToken(userPrincipal);

        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.CREATED);
    }
}
