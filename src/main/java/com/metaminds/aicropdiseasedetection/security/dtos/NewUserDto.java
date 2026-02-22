package com.metaminds.aicropdiseasedetection.security.dtos;

public record NewUserDto(
        String fullName,
        String email,
        String password,
        Long phoneNumber

){
}
