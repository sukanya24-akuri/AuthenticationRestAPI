package com.project.authify.controller;

import com.project.authify.io.ProfileRequest;
import com.project.authify.io.ProfileResponse;
import com.project.authify.service.EmailSenderService;
import com.project.authify.service.IProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController
{

    private  final IProfileService service;
    private  final EmailSenderService emailSenderService;

@PostMapping("/register")
    public ResponseEntity<?> saveProfileData( @Valid @RequestBody ProfileRequest request)
    {
        ProfileResponse result=service.createProfile(request);
        emailSenderService.sendMessage(result.getEmail(),result.getName());
        return  new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ProfileResponse readProfile(@CurrentSecurityContext(expression = "authentication?.name") String email)
    {
        return  service.getProfile(email);
    }
}
