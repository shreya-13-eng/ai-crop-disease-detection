package com.metaminds.aicropdiseasedetection.security.services;

import com.metaminds.aicropdiseasedetection.core.services.EmailService;
import com.metaminds.aicropdiseasedetection.security.dtos.NewUserDto;
import com.metaminds.aicropdiseasedetection.security.dtos.OtpVerificationDto;
import com.metaminds.aicropdiseasedetection.security.jwt.JwtUtils;
import com.metaminds.aicropdiseasedetection.security.models.User;
import com.metaminds.aicropdiseasedetection.security.repositories.UserRepository;
import com.metaminds.aicropdiseasedetection.security.responses.LoginResponse;
import com.metaminds.aicropdiseasedetection.security.responses.SignUpResponse;
import com.metaminds.aicropdiseasedetection.security.util.UserDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;

@Service
public class SignUpService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserDetailService userDetailService;

    public SignUpService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, EmailService emailService, UserDetailService userDetailService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userDetailService = userDetailService;
    }

    public SignUpResponse createUser(NewUserDto newUserDto) {
        String generatedOtp = generateOtp();
        User user = User.builder()
                .fullName(newUserDto.fullName())
                .password(passwordEncoder.encode(newUserDto.password()))
                .email(newUserDto.email())
                .phoneNumber(newUserDto.phoneNumber())
                .role("USER")
                .lastUsedOtp(Integer.valueOf(generatedOtp))
                .isActive(true)
                .build();
        emailService.sendOtpMail(user.getEmail(),user.getLastUsedOtp());
        userRepository.save(user);
        return new SignUpResponse(
                user.getEmail()
        );
    }

    public LoginResponse verifyOtp(OtpVerificationDto otpVerificationDto){
        User user = userRepository.findByEmail(otpVerificationDto.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDetail userDetail = (UserDetail) userDetailService.loadUserByUsername(otpVerificationDto.username());
        if(Objects.equals(user.getLastUsedOtp(), otpVerificationDto.otp())){
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetail,
                            null,
                          userDetail.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new LoginResponse(
                userDetail.getUsername(),
                    user.getRole(),
                    userDetail.getFullName(),
                    jwtUtils.generateJwtFromUsername(userDetail)
            );
        }else{
            throw new RuntimeException("Otp don't match.");
        }
    }


    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
