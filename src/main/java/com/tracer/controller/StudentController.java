package com.tracer.controller;

import com.tracer.model.DTO.PrivateStudentDTO;
import com.tracer.service.StudentService;
import com.tracer.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin("*")
public class StudentController {
    @Autowired
    private StudentService service;
    @Autowired
    private TokenService tokenService;


    @GetMapping("/get/student/assignments/by/username")
    public ResponseEntity<PrivateStudentDTO> getStudentAssignmentsByUsername(HttpServletRequest request, Authentication authentication) {
        tokenService.validateJwt(request, authentication);
        return new ResponseEntity<PrivateStudentDTO>(service.getStudentByEmail(authentication.getName()), HttpStatus.OK);
    }

}
