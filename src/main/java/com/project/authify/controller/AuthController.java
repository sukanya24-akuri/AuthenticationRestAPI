package com.project.authify.controller;


import com.project.authify.io.AuthRequest;
import com.project.authify.io.AuthResponse;
import com.project.authify.service.AppUserDetailsService;
import com.project.authify.service.ProfileService;
import com.project.authify.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequiredArgsConstructor
public class AuthController
{

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private  final JwtUtil jwtUtil;
    private  final ProfileService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request)
    {
        try {
            authenticate(request.getEmail(), request.getPassword());
            UserDetails userDetails=appUserDetailsService.loadUserByUsername(request.getEmail());
         String jwttoken=  jwtUtil.generateToken(userDetails);
            ResponseCookie cookie=ResponseCookie.from("jwt",jwttoken)
                    .path("/")
                    .httpOnly(true)
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                    .body(new AuthResponse(request.getEmail(),jwttoken));


        } catch (BadCredentialsException e) {

            return buildErrorResponse("email or password incorrect", HttpStatus.BAD_REQUEST);
        } catch (DisabledException e) {

            return buildErrorResponse("account is disabled", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return buildErrorResponse("authorization failed", HttpStatus.UNAUTHORIZED);
        }


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
@GetMapping("isAuthify")
public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email)
{
    return ResponseEntity.ok(email != null);
}
@PostMapping("/send-otp-email")
public void sendOtpEmail(@RequestParam String mail)
{
    try {
        profileService.sendOtp(mail);
    }
    catch (Exception e)
    {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
    }
}

    }


