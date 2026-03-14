package com.metaminds.aicropdiseasedetection.security.dtos;

public record OtpVerificationDto(
        String username,
        Integer otp
) {
}
