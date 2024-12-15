package org.example.blogapp.service;

import jakarta.transaction.Transactional;
import org.example.blogapp.dto.UserDto;
import org.example.blogapp.entity.Roles;
import org.example.blogapp.entity.User;
import org.example.blogapp.exception.ResourceNotFound;
import org.example.blogapp.repository.RolesRepo;
import org.example.blogapp.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private final RolesRepo rolesRepo;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, RolesRepo rolesRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepo = rolesRepo;
    }

    public UserDto createUser(UserDto userDto) {
        User user = new User(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            List<Roles> roles = Arrays.stream(userDto.getRoles().split(","))
                    .map(String::trim) // Remove extra spaces
                    .map(roleName -> rolesRepo.findRolesByRoleName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .toList();
            user.setRoles(roles);
        }
        else {

        }
        User savedUser = userRepo.save(user);
        return new UserDto(savedUser);
    }

    public UserDto updateUser(UserDto userDto, int id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User", "id", id));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User userUpdated = userRepo.save(user);
        return new UserDto(userUpdated);
    }

    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(int id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User", "id", id));
        return new UserDto(user);
    }

    @Transactional
    public void deleteUserById(int id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User", "id", id));

        user.getRoles().clear();
        userRepo.delete(user);
    }
}
