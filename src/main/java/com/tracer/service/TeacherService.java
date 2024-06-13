package com.tracer.service;

import com.tracer.constant.Authority;
import com.tracer.model.Role;
import com.tracer.model.Teacher;
import com.tracer.model.request.teacher.CreateTeacherRequest;
import com.tracer.model.request.teacher.EditTeacherRequest;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import com.tracer.repository.assignments.AssignmentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TeacherService implements UserDetailsService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private AssignmentsRepository assignmentsRepository;
    private final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    public Teacher createTeacher(CreateTeacherRequest request) {
        Role teacherAuthority = authorityRepository.findByAuthority(Authority.TEACHER.toString()).get();
        HashSet<Role> authorities = new HashSet<>();
        authorities.add(teacherAuthority);

        Teacher createdTeacher = new Teacher(request.username(),
                request.email(),
                request.password(),
                request.subjects(),
                authorities
                );

        saveNewTeacher(createdTeacher);

        return createdTeacher;
    }

    public Teacher editTeacher(EditTeacherRequest request, String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("No teacher could be found with username: %s", username)));

        if (!request.getUsername().isEmpty()) teacher.setUsername(request.getUsername());
        if (!request.getEmail().isEmpty()) teacher.setEmail(request.getEmail());
        if (!request.getPassword().isEmpty()) teacher.setPassword(request.getPassword());
        if (!request.getSubjects().isEmpty()) teacher.setSubjects(request.getSubjects());

        saveTeacher(teacher);


        return teacher;
    }

    public void deleteTeacherByUsername(String username) {
        teacherRepository.delete(
                teacherRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("Could not find teacher with username: %s", username))));
    }

    public void deleteTeacherById(Long id) {
        teacherRepository.delete(
                teacherRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("Could not find teacher with id: %d", id))));
    }

    public void saveTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    /**
     *  Fetches a User with a given username if the user is not found throws
     *  A UsernameNotFoundException.
     * @param username the username identifying the user whose data is required.
     * @return A UserDetails with a valid user
     * @throws UsernameNotFoundException If user was not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info(String.format("finding teacher with username: %s.", username));
        Optional<Teacher> teacherOpt = teacherRepository.findByUsername(username);
        return teacherOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
    }

    /**
     * Fetches a User with a given email if the user is not found throws a
     * UsernameNotFoundException.
     * @param email Users email address.
     * @return the user details matching the email.
     * @throws UsernameNotFoundException user was not found in the database.
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        logger.info(String.format("finding teacher with email: %s", email));
        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(email);
        return teacherOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
    }

    /**
     * Saves a new Teacher to the database.
     * @param teacher user to be saved in database.
     * @return the userDetails of that teacher.
     */
    public UserDetails saveNewTeacher(Teacher teacher) {
        logger.info("Saving new teacher.");
        return teacherRepository.save(teacher);
    }
}
