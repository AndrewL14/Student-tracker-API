package com.tracer.controller;

import com.tracer.model.Student;
import com.tracer.model.request.student.AddStudentRequest;
import com.tracer.model.request.student.EditStudentRequest;
import com.tracer.service.TeacherService;
import com.tracer.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    private TeacherService service;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents(HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateToken(servletRequest, authentication);
        return new ResponseEntity<List<Student>>(
                service.getAllStudentsByTeacherUsername(authentication.getName()),
                HttpStatus.OK
        );
    }
    @GetMapping("/unique{studentName}")
    public ResponseEntity<List<Student>> getStudentsByName(@RequestParam String studentName,
                                                           HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateToken(servletRequest, authentication);
        return new ResponseEntity<List<Student>>(
                service.getStudentsByName(authentication.getName() , studentName),
                HttpStatus.OK
        );
    }
    @PutMapping("/add")
    public ResponseEntity<List<Student>> addStudent(@RequestBody AddStudentRequest request,
                                                    HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateToken(servletRequest, authentication);
        return new ResponseEntity<List<Student>>(service.addStudent(request, authentication.getName()),
                HttpStatus.OK);
    }
    @PostMapping("/edit")
    public ResponseEntity<List<Student>> editExistingStudent(@RequestBody EditStudentRequest request,
                                                             HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateToken(servletRequest, authentication);
        return new ResponseEntity<List<Student>>(service.editExistingStudent(request, authentication.getName()),
                HttpStatus.OK);
    }
    @DeleteMapping("/delete{studentId}")
    public ResponseEntity<?> deleteExistingStudent(@RequestParam Long studentId,
                                                   HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateToken(servletRequest, authentication);
            service.deleteStudent(studentId, authentication.getName());
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
