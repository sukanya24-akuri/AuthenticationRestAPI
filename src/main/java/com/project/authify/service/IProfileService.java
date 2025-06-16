package com.project.authify.service;

import com.project.authify.io.ProfileRequest;
import com.project.authify.io.ProfileResponse;

public interface IProfileService
{
    ProfileResponse createProfile(ProfileRequest request);
}
