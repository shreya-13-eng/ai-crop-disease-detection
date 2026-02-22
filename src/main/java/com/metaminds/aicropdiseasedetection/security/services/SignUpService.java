package com.metaminds.aicropdiseasedetection.security.services;

import com.metaminds.aicropdiseasedetection.security.dtos.NewUserDto;
import com.metaminds.aicropdiseasedetection.security.jwt.JwtUtils;
import com.metaminds.aicropdiseasedetection.security.models.User;
import com.metaminds.aicropdiseasedetection.security.repositories.UserRepository;
import com.metaminds.aicropdiseasedetection.security.responses.LoginResponse;
import com.metaminds.aicropdiseasedetection.security.util.UserDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public SignUpService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    public LoginResponse createUser(NewUserDto newUserDto) {
        User user = User.builder()
                .fullName(newUserDto.fullName())
                .password(passwordEncoder.encode(newUserDto.password()))
                .email(newUserDto.email())
                .phoneNumber(newUserDto.phoneNumber())
                .role("USER")
                .isActive(true)
                .build();
        userRepository.save(user);
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(newUserDto.email(), newUserDto.password()));
        }catch(AuthenticationException e){
            throw new BadCredentialsException(e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(authentication.getPrincipal() == null){
            throw new BadCredentialsException("Bad credentials");
        }
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        return new LoginResponse(
                userDetail.getUsername(),
                userDetail.getAuthorities().iterator().next().getAuthority(),
                userDetail.getFullName(),
                jwtUtils.generateJwtFromUsername(userDetail)
        );
    }
}
