package com.project.authify.service;

import com.project.authify.entity.UserEntity;
import com.project.authify.io.ProfileRequest;
import com.project.authify.io.ProfileResponse;
import com.project.authify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.UUID;

@Service
public class ProfileService implements IProfileService
{

    @Autowired
    private UserRepository repo;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newUser = convertToEntity(request);
        if (!repo.existsByEmail(request.getEmail())) {
            newUser = repo.save(newUser);
            return convertToProfileResponce(newUser);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");

    }

    private UserEntity convertToEntity(ProfileRequest request)
    {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .isverifired(false)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .verifyotpExpireAt(0L)
                .userId(UUID.randomUUID().toString())
                .verifyOpt(null)
                .build();
    }


    private ProfileResponse convertToProfileResponce(UserEntity entity)
    {
      return   ProfileResponse.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .userId(entity.getUserId())
                .isverifired(entity.getIsverifired())
                .build();
    }
}
