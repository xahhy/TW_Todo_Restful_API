package com.thoughtworks.training.restfulapi.security;

import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class LoginFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get token from header
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null){
            User userFromToken = tokenService.getUserFromToken(header);
            // verify token
            if (userFromToken == null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            // put user to securtiy context
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userFromToken, "", new ArrayList<>())
            );
        }
        filterChain.doFilter(request, response);
    }
}
