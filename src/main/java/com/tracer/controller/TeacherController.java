package com.tracer.controller;

import com.tracer.model.DTO.PublicStudentDTO;
import com.tracer.model.DTO.TeacherStudentList;
import com.tracer.model.users.Teacher;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;
import com.tracer.model.request.assignment.AddAssignmentRequest;
import com.tracer.model.request.assignment.DeleteAssignmentRequest;
import com.tracer.model.request.assignment.EditAssignmentRequest;
import com.tracer.model.request.assignment.UpdateAssignmentStatusRequest;
import com.tracer.model.request.student.AddStudentRequest;
import com.tracer.model.request.student.DeleteStudentRequest;
import com.tracer.model.request.student.EditStudentRequest;
import com.tracer.model.request.teacher.EditTeacherRequest;
import com.tracer.service.AssignmentsService;
import com.tracer.service.StudentService;
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
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private AssignmentsService assignmentsService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/edit/teacher")
    public ResponseEntity<Teacher> editTeacher(HttpServletRequest servletRequest, EditTeacherRequest request, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<Teacher>(
                teacherService.editTeacher(request, authentication.getName()), HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/teacher")
    public ResponseEntity<?> deleteTeacher(HttpServletRequest request, Authentication authentication) {
        tokenService.validateJwt(request, authentication);
        teacherService.deleteTeacherByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<TeacherStudentList> getAllStudents(HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<TeacherStudentList>(
                studentService.getAllStudentsByTeacherUsername(authentication.getName()),
                HttpStatus.OK
        );
    }
    @GetMapping("/get-student{id}")
    public ResponseEntity<PublicStudentDTO> getStudentById(@RequestParam Long id) {
        return new ResponseEntity<PublicStudentDTO>(studentService.getStudentById(id), HttpStatus.OK);
    }
    @GetMapping("/unique{studentName}")
    public ResponseEntity<PublicStudentDTO> getStudentsByName(@RequestParam String studentName,
                                                              HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<PublicStudentDTO>(
                studentService.getStudentByName(authentication.getName() , studentName),
                HttpStatus.OK
        );
    }
    @PutMapping("/add/student")
    public ResponseEntity<TeacherStudentList> addStudent(@RequestBody AddStudentRequest request,
                                                    HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<TeacherStudentList>(studentService.addStudent(request, authentication.getName()),
                HttpStatus.OK);
    }
    @PostMapping("/edit/student")
    public ResponseEntity<PublicStudentDTO> editExistingStudent(@RequestBody EditStudentRequest request,
                                                             HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<PublicStudentDTO>(studentService.editExistingStudent(request),
                HttpStatus.OK);
    }
    @DeleteMapping("/delete/student")
    public ResponseEntity<?> deleteExistingStudent(@RequestBody DeleteStudentRequest request,
                                                   HttpServletRequest servletRequest, Authentication authentication) {
        tokenService.validateJwt(servletRequest, authentication);
            studentService.deleteStudent(request, authentication.getName());
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PutMapping("/add/assignment")
    public ResponseEntity<Assignments> addNewAssignment(@RequestBody AddAssignmentRequest request,
                                                        HttpServletRequest servletRequest, Authentication authentication ) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<Assignments>(
                assignmentsService.addNewAssignment(request), HttpStatus.CREATED
        );
    }

    @GetMapping("/get-all{id}")
    public ResponseEntity<List<Assignments>> getAllAssignmentsByStudentId(@RequestParam Long id,
                                                                          HttpServletRequest servletRequest, Authentication authentication ) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<List<Assignments>>(
                assignmentsService.getAllAssignmentsByStudentId(id), HttpStatus.OK
        );
    }

    @PostMapping("/update-status/")
    public ResponseEntity<List<Assignment>> updateStatus(@RequestBody UpdateAssignmentStatusRequest request,
                                                         HttpServletRequest servletRequest, Authentication authentication ) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<List<Assignment>>(
                assignmentsService.updateCompleteStatus(request, authentication.getName()), HttpStatus.OK
        );
    }

    @PostMapping("/edit/assignment")
    public ResponseEntity<List<Assignment>> editAssignment(@RequestBody EditAssignmentRequest request,
                                                           HttpServletRequest servletRequest, Authentication authentication ) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<List<Assignment>>(
                assignmentsService.editAssignment(request), HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/assignment")
    public ResponseEntity<List<Assignment>> deleteAssignment(@RequestBody DeleteAssignmentRequest request,
                                                             HttpServletRequest servletRequest, Authentication authentication ) {
        tokenService.validateJwt(servletRequest, authentication);
        return new ResponseEntity<List<Assignment>>(
                assignmentsService.deleteAssignment(request), HttpStatus.OK
        );
    }
}
