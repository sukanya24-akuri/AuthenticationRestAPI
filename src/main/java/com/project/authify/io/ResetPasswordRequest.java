package com.project.authify.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest
{
    @NotBlank(message = "email is requires")
    @Email
    private  String email;
    @NotBlank(message = "otp is required")
    private String otp;
    @NotBlank
    private String newPassword;
}
