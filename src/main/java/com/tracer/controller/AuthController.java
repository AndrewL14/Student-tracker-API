package com.tracer.controller;

import com.tracer.model.request.authentication.BasicLoginRequest;
import com.tracer.model.request.authentication.CompletePasswordResetRequest;
import com.tracer.model.request.authentication.EmailLoginRequest;
import com.tracer.model.request.authentication.RegistrationRequest;
import com.tracer.model.response.StudentLoginResponse;
import com.tracer.model.response.TeacherLoginResponse;
import com.tracer.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<TeacherLoginResponse> registerUser(@RequestBody RegistrationRequest request) {
        return new ResponseEntity<TeacherLoginResponse>(
                authService.registerUser(request.getUsername(), request.getEmail() , request.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/login/basic")
    public ResponseEntity<TeacherLoginResponse>  loginUserByUsername(@RequestBody BasicLoginRequest request) {
        return new ResponseEntity<TeacherLoginResponse>(
          authService.loginTeacherByUsername(request.getUsername(), request.getPassword()), HttpStatus.OK
        );
    }

    @PostMapping("/login/email")
    public ResponseEntity<TeacherLoginResponse>  loginUserByEmail(@RequestBody EmailLoginRequest request) {
        return new ResponseEntity<TeacherLoginResponse>(
                authService.loginTeacherByEmail(request.getEmail(), request.getPassword()), HttpStatus.OK
        );
    }

    @PostMapping("/student/login")
    public ResponseEntity<StudentLoginResponse> studentLogin(@RequestBody EmailLoginRequest request) {
        return new ResponseEntity<StudentLoginResponse>(
                authService.loginStudentByEmail(request.getEmail() , request.getPassword()), HttpStatus.OK
        );
    }

    @GetMapping("/verify/email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token, Authentication authentication) {
        return new ResponseEntity<String>(authService.verifyEmail(token, authentication.getName()) , HttpStatus.OK
        );
    }
    @GetMapping("verify/send{email}")
    public ResponseEntity<String> sendEmailVerification(@RequestParam String email) {
        authService.sendEmailVerification(email);
        return new ResponseEntity<String>("201", HttpStatus.OK);
    }

    @GetMapping("/initiate-reset{email}")
    public void initiatePasswordReset(@RequestParam String email) {
        authService.initiatePasswordReset(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<TeacherLoginResponse> completePasswordReset(@RequestBody CompletePasswordResetRequest request) {
        return new ResponseEntity<TeacherLoginResponse>(
                authService.completePasswordReset(request.getToken() , request.getPassword()), HttpStatus.OK
        );
    }

    @PostMapping("/generate/refresh-token")
    public ResponseEntity<String> generateRefreshToken(Authentication authentication) {
        return new ResponseEntity<String>(
                authService.generateRefreshToken(authentication), HttpStatus.OK
        );
    }

    @PostMapping("/refresh-token{token}")
    public ResponseEntity<String> refreshToken(@RequestParam String token) {
        return new ResponseEntity<String>(
                authService.refreshToken(token), HttpStatus.OK
        );
    }
}
