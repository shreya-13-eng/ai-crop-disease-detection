package com.metaminds.aicropdiseasedetection.security.responses;

public record LoginResponse(
        String userName,
        String role,
        String fullName,
        String token
) {
}
