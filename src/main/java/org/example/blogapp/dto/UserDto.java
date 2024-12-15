package org.example.blogapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.blogapp.entity.Roles;
import org.example.blogapp.entity.User;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotEmpty
    @NotNull
    @NotBlank
    private String name;
    @Email
    private String email;
    private String password;
    private String about;
    private String roles;

    public UserDto(User user)
    {
//        ModelMapper mapper=new ModelMapper();
//        UserDto  userDto=mapper.map(user,UserDto.class);
        this.id= user.getId();
        this.name= user.getName();
        this.email= user.getEmail();
        this.password= user.getPassword();
        this.about= user.getAbout();
        this.roles = user.getRoles().stream()
                .map(Roles::getRoleName)
                .collect(Collectors.joining(", "));

    }
}
