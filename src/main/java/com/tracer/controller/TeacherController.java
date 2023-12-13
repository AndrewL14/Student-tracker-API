package com.tracer.controller;

import com.tracer.model.Student;
import com.tracer.model.request.AddStudentRequest;
import com.tracer.model.request.EditStudentRequest;
import com.tracer.model.request.GetStudentRequest;
import com.tracer.service.TeacherService;
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

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents(Authentication authentication) {
        return new ResponseEntity<List<Student>>(
                service.getAllStudentsByTeacherUsername(authentication.getName()),
                HttpStatus.OK
        );
    }
    @GetMapping("/unique{studentName}")
    public ResponseEntity<List<Student>> getStudentsByName(@RequestParam String studentName, Authentication authentication) {
        return new ResponseEntity<List<Student>>(
                service.getStudentsByName(authentication.getName() , studentName),
                HttpStatus.OK
        );
    }
    @PutMapping("/add")
    public ResponseEntity<List<Student>> addStudent(@RequestBody AddStudentRequest request,
                                                    Authentication authentication) {
        return new ResponseEntity<List<Student>>(service.addStudent(request, authentication.getName()),
                HttpStatus.OK);
    }
    @PostMapping("/edit")
    public ResponseEntity<List<Student>> editExistingStudent(@RequestBody EditStudentRequest request,
                                                             Authentication authentication) {
        return new ResponseEntity<List<Student>>(service.editExistingStudent(request, authentication.getName()),
                HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteExistingStudent(@RequestBody GetStudentRequest request,
                                                   Authentication authentication) {
            service.deleteStudent(request, authentication.getName());
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
