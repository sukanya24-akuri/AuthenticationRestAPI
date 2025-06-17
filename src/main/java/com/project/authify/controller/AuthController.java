package com.project.authify.controller;


import com.project.authify.io.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



@RestController
@RequiredArgsConstructor
public class AuthController
{

    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request)
    {
        try {
            authenticate(request.getEmail(), request.getPassword());
        } catch (BadCredentialsException e) {

            return buildErrorResponse("email or password incorrect", HttpStatus.BAD_REQUEST);
        } catch (DisabledException e) {

            return buildErrorResponse("account is disabled", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return buildErrorResponse("authorization failed", HttpStatus.UNAUTHORIZED);
        }
        Map<String, Object> success = new HashMap<>();
        success.put("error", false);
        success.put("message", "login successful");
        return ResponseEntity.ok(success);

    }
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("error", true);
        map.put("message", message);
        return ResponseEntity.status(status).body(map);
    }

        private void authenticate (String email, String password)
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }


    }


