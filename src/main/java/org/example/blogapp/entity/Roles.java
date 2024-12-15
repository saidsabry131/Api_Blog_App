package org.example.blogapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Roles {

    @Id
    private int id;

    @Column(name = "name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
