package com.project.authify.service;

import com.project.authify.entity.UserEntity;
import com.project.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService  implements UserDetailsService
{
    private UserRepository repo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existingUser=repo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("email id not found"));
        return new User(existingUser.getEmail(),existingUser.getPassword(),new ArrayList<>());
    }
}
