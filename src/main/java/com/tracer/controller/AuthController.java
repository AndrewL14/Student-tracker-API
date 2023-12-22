package com.tracer.controller;

import com.tracer.model.request.LoginRequest;
import com.tracer.model.request.RegistrationRequest;
import com.tracer.model.request.ValidateTokenRequest;
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
                authService.registerUser(request.getUsername(), request.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>  loginUser(@RequestBody LoginRequest request) {
        return new ResponseEntity<LoginResponse>(
          authService.loginUser(request.getUsername(), request.getPassword()), HttpStatus.OK
        );
    }
}
