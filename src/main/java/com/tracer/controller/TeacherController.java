package com.tracer.controller;

import com.tracer.model.Student;
import com.tracer.model.request.AddStudentRequest;
import com.tracer.model.request.EditStudentRequest;
import com.tracer.model.request.GetStudentRequest;
import com.tracer.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    private TeacherService service;

    @GetMapping("/all{username}")
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam String username) {
        return new ResponseEntity<List<Student>>(
                service.getAllStudentsByTeacherUsername(username),
                HttpStatus.OK
        );
    }
    @GetMapping("/unique")
    public ResponseEntity<List<Student>> getStudentsByName(@RequestBody GetStudentRequest request) {
        return new ResponseEntity<List<Student>>(
                service.getStudentsByName(request.getTeacherUsername() , request.getStudentName()),
                HttpStatus.OK
        );
    }
    @PutMapping("/add")
    public ResponseEntity<List<Student>> addStudent(@RequestBody AddStudentRequest request) {
        return new ResponseEntity<List<Student>>(service.addStudent(request),
                HttpStatus.OK);
    }
    @PostMapping("/edit")
    public ResponseEntity<List<Student>> editExistingStudent(@RequestBody EditStudentRequest request) {
        return new ResponseEntity<List<Student>>(service.editExistingStudent(request),
                HttpStatus.OK);
    }
    @GetMapping("/delete")
    public ResponseEntity<?> deleteExistingStudent(@RequestBody GetStudentRequest request) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
