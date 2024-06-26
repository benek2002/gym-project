package com.example.backend.controller;

import com.example.backend.Service.AuthenticationService;
import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.LoginDto;
import com.example.backend.dto.RegisterDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(authenticationService.register(registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authenticationService.login(loginDto));
    }
    @GetMapping("/secured")
    public ResponseEntity<String> testOfSecurity(){
        return ResponseEntity.ok("Test of security");
    }



}
