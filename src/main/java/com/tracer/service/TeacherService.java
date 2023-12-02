package com.tracer.service;

import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.model.request.AddStudentRequest;
import com.tracer.model.request.EditStudentRequest;
import com.tracer.model.request.GetStudentRequest;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherService implements UserDetailsService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    /**
     * Uses the teachers unique username to find the list of corresponding students.
     * @param username Teacher unique username
     * @return a list of students matching the teacher.
     */
    public List<Student> getAllStudentsByTeacherUsername(String username) {
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(NullPointerException::new);

        return teacher.getStudents();
    }

    /**
     * Gets a list of students based on the teachers username and the students full name.
     * Returns a list in the event there is more than 1 student with the same name.
     * @param teacherUsername Teachers username
     * @param studentName student's full name
     * @return A list of students matching studentName
     */
    public List<Student> getStudentsByName(String teacherUsername, String studentName) {
        Teacher teacher = teacherRepository.findByUsername(teacherUsername)
                .orElseThrow(NullPointerException::new);

        return teacher.getStudents().stream()
                .filter(student -> Objects.equals(student.getName(), studentName))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new Student to add to an existing teacher.
     * @param request request body with all the information needed to create a student
     * @return An updated list of students
     */
    public List<Student> addStudent(AddStudentRequest request) {
        Teacher teacher = teacherRepository.findByUsername(request.getTeacherUsername())
                .orElseThrow(NullPointerException::new);
        Student studentToAdd = new Student(request.getName() , request.getPeriod(), request.getGrade());
        List<Student> students = teacher.getStudents();
        students.add(studentToAdd);
        teacher.setStudents(students);
        teacherRepository.save(teacher);
        studentRepository.save(studentToAdd);
        return students;
    }

    /**
     * Edits an existing student's data
     * @param request request body with all information needed to handle the edit
     * @return an updated List of students
     */
    public List<Student> editExistingStudent(EditStudentRequest request) {
        Teacher teacher = teacherRepository.findByUsername(request.getTeacherUsername())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        List<Student> updatedStudents = teacher.getStudents().stream()
                .filter(student -> student.getName().equals(request.getName()) && student.getPeriod().equals(request.getPeriod()))
                .peek(student -> {
                    request.getGradeToChange().ifPresent(grade -> student.setGrade(BigDecimal.valueOf(grade)));
                    request.getNameToChange().ifPresent(student::setName);
                    request.getPeriodToChange().ifPresent(student::setPeriod);
                    studentRepository.save(student);
                })
                .collect(Collectors.toList());

        teacherRepository.save(teacher);

        return updatedStudents;
    }

    /**
     * Deletes a student if existed.
     * @param request request body containing the teacher username and student full name
     */
    public void deleteStudent(GetStudentRequest request) {
        Teacher teacher = teacherRepository.findByUsername(request.getTeacherUsername())
                .orElseThrow(NullPointerException::new);
        Optional<Student> studentToDelete = teacher.getStudents().stream()
                        .filter(student -> student.getName().equals(request.getStudentName()))
                                .findFirst();
        studentToDelete.ifPresent(teacher.getStudents()::remove);
        studentToDelete.ifPresent(student -> {
            studentRepository.delete(student);
            teacherRepository.save(teacher);
        });
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
        Optional<Teacher> teacherOpt = teacherRepository.findByUsername(username);
        return teacherOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
    }
}
