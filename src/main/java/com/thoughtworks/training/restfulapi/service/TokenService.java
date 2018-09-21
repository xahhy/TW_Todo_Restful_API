package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.exceptions.UnauthorizedException;
import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.persist.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    UserService userService;

    public String createToken(User user) {
        String token = Jwts.builder()
                .claim("userId", user.getId())
                .signWith(SignatureAlgorithm.HS512, "password".getBytes())
                .compact();
        return token;
    }

    public User getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("password".getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return userService.getUserById(Long.parseLong(claims.get("userId").toString()));
        }catch (Exception exception){
            throw new UnauthorizedException();
        }
    }

    public static User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
