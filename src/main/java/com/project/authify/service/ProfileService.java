package com.project.authify.service;

import com.project.authify.entity.UserEntity;
import com.project.authify.io.ProfileRequest;
import com.project.authify.io.ProfileResponse;
import com.project.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


@RequiredArgsConstructor
@Service
public class ProfileService implements IProfileService
{


    private final UserRepository repo;
    private  final PasswordEncoder passwordEncoder;
    private  final EmailSenderService emailSenderService;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newUser = convertToEntity(request);
        if (!repo.existsByEmail(request.getEmail())) {
            newUser = repo.save(newUser);
            return convertToProfileResponse(newUser);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");

    }

    @Override
    public ProfileResponse getProfile(String email) {
      UserEntity existingUser=  repo.findByEmail(email)
              .orElseThrow(()->new UsernameNotFoundException("user not found"+email));
     return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendOtp(String email)
    {
       UserEntity existingUser= repo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));

       //6 digit opt
     String otp=   String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        //otp expiration

      long expireOtp=  System.currentTimeMillis()+(15*60*1000);
      //update the otp to user
      existingUser.setResetOtp(otp);
      existingUser.setResetOtpExpireAt(expireOtp);
      repo.save(existingUser);
      try
      {
//sent to email otp
          emailSenderService.sendOtpMail(existingUser.getEmail(), otp);
      } catch (RuntimeException e)
      {
          e.getMessage();
      }


    }

    private UserEntity convertToEntity(ProfileRequest request)
    {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isverifired(false)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .verifyotpExpireAt(0L)
                .userId(UUID.randomUUID().toString())
                .verifyOpt(null)
                .build();
    }


    private ProfileResponse convertToProfileResponse(UserEntity entity)
    {
      return   ProfileResponse.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .userId(entity.getUserId())
                .isverifired(entity.getIsverifired())
                .build();
    }
}
