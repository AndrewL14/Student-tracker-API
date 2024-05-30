package com.tracer.controller;

import com.tracer.model.DTO.PublicStudentDTO;
import com.tracer.model.DTO.TeacherStudentList;
import com.tracer.model.Student;
import com.tracer.model.assignments.Assignments;
import com.tracer.model.request.student.AddStudentRequest;
import com.tracer.model.request.student.DeleteStudentRequest;
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
    public ResponseEntity<TeacherStudentList> getAllStudents(HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<TeacherStudentList>(
                service.getAllStudentsByTeacherUsername(authentication.getName()),
                HttpStatus.OK
        );
    }
    @GetMapping("/unique{studentName}")
    public ResponseEntity<PublicStudentDTO> getStudentsByName(@RequestParam String studentName,
                                                              HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<PublicStudentDTO>(
                service.getStudentByName(authentication.getName() , studentName),
                HttpStatus.OK
        );
    }
    @PutMapping("/add")
    public ResponseEntity<TeacherStudentList> addStudent(@RequestBody AddStudentRequest request,
                                                    HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<TeacherStudentList>(service.addStudent(request, authentication.getName()),
                HttpStatus.OK);
    }
    @PostMapping("/edit")
    public ResponseEntity<PublicStudentDTO> editExistingStudent(@RequestBody EditStudentRequest request,
                                                             HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<PublicStudentDTO>(service.editExistingStudent(request),
                HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteExistingStudent(@RequestBody DeleteStudentRequest request,
                                                   HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
            service.deleteStudent(request, authentication.getName());
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
