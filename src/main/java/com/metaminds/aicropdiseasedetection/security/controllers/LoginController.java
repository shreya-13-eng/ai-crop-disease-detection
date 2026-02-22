package com.metaminds.aicropdiseasedetection.security.controllers;

import com.metaminds.aicropdiseasedetection.security.dtos.LoginDto;
import com.metaminds.aicropdiseasedetection.security.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public/auth")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto
    ) {
        try{
            var response = loginService.doLogin(loginDto);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message","Unauthorized");
            map.put("error",e.getMessage());
            map.put("status", HttpStatus.UNAUTHORIZED);
            map.put("path","/login");
            return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
        }
    }
}
