package com.tracer.service;

import com.tracer.model.Role;
import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.model.response.LoginResponse;
import com.tracer.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AuthorityRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public LoginResponse registerUser(String username, String password){

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("TEACHER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        Teacher teacher = (Teacher) teacherService.saveNewTeacher(new Teacher(username, encodedPassword, new ArrayList<>(),
                authorities));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateJwt(authentication);
        return new LoginResponse(teacher.getStudents(), token);
    }

    public LoginResponse loginUser(String username, String password){

        Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.generateJwt(authentication);
        Teacher teacher = (Teacher) teacherService.loadUserByUsername(username);
        List<Student> students = teacher.getStudents();
        return  new LoginResponse(students, token);

    }
}
