package com.metaminds.aicropdiseasedetection.security.controllers;

import com.metaminds.aicropdiseasedetection.security.dtos.NewUserDto;
import com.metaminds.aicropdiseasedetection.security.services.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/public")
public class SignUpController {
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @RequestBody NewUserDto newUserDto
    ) {
        try {
            var response = signUpService.createUser(newUserDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message","User not created");
            map.put("error",e.getMessage());
            map.put("status", HttpStatus.BAD_REQUEST);
            map.put("path","/sign-up");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }
}
