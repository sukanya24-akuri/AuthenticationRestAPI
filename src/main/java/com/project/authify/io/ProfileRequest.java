package com.project.authify.io;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
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
    @NotNull(message = "name should not be blank")
    private String name;
    @NotNull(message = "email already exists")
    @Column(unique = true)
    private String email;
    @Size(min = 6,message = "password altest 6 characters")
    private String password;
}
