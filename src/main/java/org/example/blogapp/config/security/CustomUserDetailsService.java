package org.example.blogapp.config.security;

import org.example.blogapp.entity.User;
import org.example.blogapp.exception.ResourceNotFound;
import org.example.blogapp.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findUsersByEmail(username)
                .orElseThrow(()->new ResourceNotFound("User","Email",username));
        return new UserPrincipal(user);
    }
}
