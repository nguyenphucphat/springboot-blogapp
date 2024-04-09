package com.springboot.blog.payload;

import com.springboot.blog.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @NotEmpty(message = "Username is required")
    @Size(min = 2, max = 15, message = "Username must be between 2 and 15 characters")
    private String username;
    @NotEmpty(message = "Password is required")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    private String password;
    private Set<Role> roles = new HashSet<>();
}
