package com.tracer.controller;

import com.tracer.model.request.LoginRequest;
import com.tracer.model.response.LoginResponse;
import com.tracer.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest request) {
        return authService.loginUser(request.getUsername(), request.getPassword());
    }
}
