package com.tracer.controller;

import com.tracer.model.Teacher;
import com.tracer.model.request.LoginRequest;
import com.tracer.model.request.RegistrationRequest;
import com.tracer.model.response.LoginResponse;
import com.tracer.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public Teacher registerUser(@RequestBody RegistrationRequest request) {
        return authService.registerUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest request) {
        return authService.loginUser(request.getUsername(), request.getPassword());
    }
}
