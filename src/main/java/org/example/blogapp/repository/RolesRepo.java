package org.example.blogapp.repository;

import org.example.blogapp.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepo  extends JpaRepository<Roles,Integer> {
    Optional<Roles> findRolesByRoleName(String name);

}
