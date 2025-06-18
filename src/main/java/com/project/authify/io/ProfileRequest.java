package com.project.authify.io;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProfileRequest
{
    @NotBlank(message = "name should not be blank")
    private String name;

    @NotNull(message = "Email should not be blank")
    @Email(message = "enter valid email")
    private String email;

    @Size(min = 6,message = "password atleast 6 characters")
    private String password;
}
