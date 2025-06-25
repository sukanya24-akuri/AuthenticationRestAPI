package com.project.authify.service;

import com.project.authify.io.ProfileRequest;
import com.project.authify.io.ProfileResponse;

public interface IProfileService
{
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getProfile(String email);
    void sendOtp(String email);
    void setPassowordWithOtp(String email,String otp,String newPassword);
    void sendOtpToEmail(String email);
    void verifyOtp(String email,String otp);

}
