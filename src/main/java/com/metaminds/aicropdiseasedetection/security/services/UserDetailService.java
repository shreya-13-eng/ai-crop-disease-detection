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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;


    public UserDetailService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UserDetail(user);
    }
}
