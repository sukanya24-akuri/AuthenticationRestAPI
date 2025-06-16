package com.project.authify.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProfileResponse
{
    private String name;
    private String email;
    private String userId;
    private Boolean isverifired;
}
