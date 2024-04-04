package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Username is required")
    @Size(min = 2, max = 20)
    private String username;

    @NotEmpty(message = "Email is required")
    @Email
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 4)
    private String password;
}
