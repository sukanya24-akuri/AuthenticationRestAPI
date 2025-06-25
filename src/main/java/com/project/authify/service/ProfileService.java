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

    @Override
    public void setPassowordWithOtp(String email, String otp, String newPassword) {
      UserEntity existingUser=  repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found"+email));
      if(existingUser.getResetOtp()==null || ! existingUser.getResetOtp().equals(otp))
      {
          throw new RuntimeException("invalid otp");
      }
      if(existingUser.getResetOtpExpireAt()<System.currentTimeMillis())
      {
          throw new RuntimeException("otp is expired");
      }
      existingUser.setPassword(passwordEncoder.encode(newPassword));

        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(0l);
        repo.save(existingUser);

    }

    @Override
    public void sendOtpToEmail(String email)
    {
        UserEntity existingUser= repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found "+email));
        if(existingUser.getIsAccountverified()!=null && existingUser.getIsAccountverified())
        {
            return;
        }
        String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        //1 day
        long expireOtp=System.currentTimeMillis()+(24*60*60*1000);

        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expireOtp);
        repo.save(existingUser);
try
{
    emailSenderService.verifyEmailOtp(existingUser.getEmail(),otp);
}
catch (Exception e)
{
  throw new RuntimeException("unable to send email");
}
    }

    @Override
    public void verifyOtp(String email, String otp) {
UserEntity existingUser=repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found"+email));
if(existingUser.getVerifyOtp()==null || ! existingUser.getVerifyOtp().equals(otp))
{
    throw  new RuntimeException("invalid otp");
}
if(existingUser.getVerifyOtpExpireAt()<System.currentTimeMillis())
{
    throw  new RuntimeException(" otp expired");
}
existingUser.setIsAccountverified(true);
existingUser.setVerifyOtpExpireAt(0l);
existingUser.setVerifyOtp(null);
repo.save(existingUser);

    }

    private UserEntity convertToEntity(ProfileRequest request)
    {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountverified(false)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .verifyOtpExpireAt(0L)
                .userId(UUID.randomUUID().toString())
                .verifyOtp(null)
                .build();
    }


    private ProfileResponse convertToProfileResponse(UserEntity entity)
    {
      return   ProfileResponse.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .userId(entity.getUserId())
                .isverifired(entity.getIsAccountverified())
                .build();
    }
}
