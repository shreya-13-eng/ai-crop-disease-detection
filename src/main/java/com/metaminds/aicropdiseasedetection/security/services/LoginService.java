package com.metaminds.aicropdiseasedetection.security.services;

import com.metaminds.aicropdiseasedetection.security.dtos.LoginDto;
import com.metaminds.aicropdiseasedetection.security.jwt.JwtUtils;
import com.metaminds.aicropdiseasedetection.security.responses.LoginResponse;
import com.metaminds.aicropdiseasedetection.security.util.UserDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public LoginService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse doLogin(
            LoginDto loginDto
    ){
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));
        }catch(AuthenticationException e){
            throw new BadCredentialsException(e.getMessage());
        }
        if(authentication.getPrincipal() == null){
            throw new BadCredentialsException("Invalid username or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        return new LoginResponse(
                userDetail.getUsername(),
                userDetail.getAuthorities().iterator().next().getAuthority(),
                userDetail.getFullName(),
                jwtUtils.generateJwtFromUsername(userDetail)
        );
    }
}
