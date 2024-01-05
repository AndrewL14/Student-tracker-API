package com.tracer.controller;

import com.tracer.model.request.BasicLoginRequest;
import com.tracer.model.request.EmailLoginRequest;
import com.tracer.model.request.RegistrationRequest;
import com.tracer.model.response.LoginResponse;
import com.tracer.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody RegistrationRequest request) {
        return new ResponseEntity<LoginResponse>(
                authService.registerUser(request.getUsername(), request.getEmail() , request.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/login/basic")
    public ResponseEntity<LoginResponse>  loginUserByUsername(@RequestBody BasicLoginRequest request) {
        return new ResponseEntity<LoginResponse>(
          authService.loginUserByUsername(request.getUsername(), request.getPassword()), HttpStatus.OK
        );
    }

    @PostMapping("/login/email")
    public ResponseEntity<LoginResponse>  loginUserByEmail(@RequestBody EmailLoginRequest request) {
        return new ResponseEntity<LoginResponse>(
                authService.loginUserByEmail(request.getEmail(), request.getPassword()), HttpStatus.OK
        );
    }

    @GetMapping("/verify/email{token}")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        return new ResponseEntity<String>(authService.verifyEmail(token) , HttpStatus.OK
        );
    }
}
