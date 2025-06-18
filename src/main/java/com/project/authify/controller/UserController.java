package com.project.authify.controller;

import com.project.authify.io.ProfileRequest;
import com.project.authify.io.ProfileResponse;
import com.project.authify.service.IProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController
{

    private  final IProfileService service;

@PostMapping("/register")
    public ResponseEntity<?> saveProfileData( @Valid @RequestBody ProfileRequest request)
    {
        ProfileResponse result=service.createProfile(request);
        return  new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
