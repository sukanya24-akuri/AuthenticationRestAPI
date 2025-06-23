package com.project.authify.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil
{
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails userDetails)
    {
        Map<String,Object> claims= new HashMap<>();
        return createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username)
    {
  return Jwts.builder()
          .setClaims(claims)
          .setSubject(username)

          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
          .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
          .compact();
    }


    public Claims extractAllClaims(String token)
    {
       return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

   public  <T> T extractClaim(String token, Function<Claims,T> claimsReslover)
   {
       Claims claims=extractAllClaims(token);
       return claimsReslover.apply(claims);
   }


    public String  extractEmail(String token)
    {
        return  extractClaim(token,Claims::getSubject);
    }


    public Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    private Boolean isTokenExpiration(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token,UserDetails userDetails)
    {
        final String email = extractEmail(token);
        System.out.println(email+"emialll");
        return (email.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }

}
