package com.tracer.controller;

import com.tracer.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin("*")
public class TeacherController {
    @GetMapping("all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return null;
//        return new ResponseEntity<List<Student>>(null);
    }
    @GetMapping("unique")
    public ResponseEntity<List<Student>> getStudentsByName() {
        return null;
//        return new ResponseEntity<List<Student>>(null);
    }
    @PutMapping("add")
    public ResponseEntity<Student> addStudent() {
        return null;
//        return new ResponseEntity<Student>(null);
    }
    @PostMapping("edit")
    public ResponseEntity<Student> editExistingStudent() {
        return null;
//        return new ResponseEntity<Student>(null);
    }
    @GetMapping("delete")
    public ResponseEntity<Boolean> deleteExistingStudent() {
        return null;
//        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }
}
