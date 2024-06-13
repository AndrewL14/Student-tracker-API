package com.tracer.service;

import com.tracer.converter.ModelConverter;
import com.tracer.model.DTO.PrivateStudentDTO;
import com.tracer.model.DTO.PublicStudentDTO;
import com.tracer.model.DTO.TeacherStudentList;
import com.tracer.model.Role;
import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;
import com.tracer.model.request.assignment.AddAssignmentRequest;
import com.tracer.model.request.assignment.DeleteAssignmentRequest;
import com.tracer.model.request.assignment.EditAssignmentRequest;
import com.tracer.model.request.assignment.UpdateAssignmentStatusRequest;
import com.tracer.model.request.student.AddStudentRequest;
import com.tracer.model.request.student.DeleteStudentRequest;
import com.tracer.model.request.student.EditStudentRequest;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.TeacherRepository;
import com.tracer.repository.assignments.AssignmentRepository;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.assignments.AssignmentsRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class StudentService implements UserDetailsService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentsRepository assignmentsRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    /**
     * Gets a specific student by their ID.
     * @param id Database Id
     * @return The student associated with the ID.
     */
    public PublicStudentDTO getStudentById(Long id) {
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find student with id: %d", id)));
        return ModelConverter.studentToPublicDTO(
                student,
                student.getTeacher().getSubjects()
        );
    }

    /**
     * Gets a specific student by their ID.
     * @param email Students Unique email.
     * @return The student associated with the ID.
     */
    public PrivateStudentDTO getStudentByEmail(String email) {
        return ModelConverter.studentToPrivateDTO(
                studentRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find student with email: %s", email)))
        );
    }

    /**
     * Uses the teachers unique username to find the list of corresponding students.
     * @param username Teacher unique username
     * @return a list of students matching the teacher.
     */
    public TeacherStudentList getAllStudentsByTeacherUsername(String username) {
        logger.info(String.format("Getting all students associated with teacher: %s", username));
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(NullPointerException::new);
        return ModelConverter.teacherStudentListToDTO(teacher.getStudents(), teacher.getSubjects());
    }

    /**
     * Gets a list of students based on the teachers username and the students full name.
     * Returns a list in the event there is more than 1 student with the same name.
     * @param teacherUsername Teachers username
     * @param studentName student's full name
     * @return A list of students matching studentName
     */
    public PublicStudentDTO getStudentByName(String teacherUsername, String studentName) {
        logger.info(String.format("Getting student with name %s associated with %s.", studentName, teacherUsername));

        Teacher teacher = teacherRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with username: " + teacherUsername));

        List<Student> students = teacher.getStudents();

        Optional<Student> foundStudent = students.stream()
                .filter(student -> student.getName().equals(studentName))
                .findFirst();

        if (foundStudent.isPresent()) {
            Student student = foundStudent.get();
            return ModelConverter.studentToPublicDTO(student, teacher.getSubjects());
        }
        logger.warn(String.format("Student with name %s not found for teacher %s.", studentName, teacherUsername));

        return null;
    }

    /**
     * Creates a new Student to add to an existing teacher.
     * @param request request body with all the information needed to create a student
     * @return An updated list of students
     */
    public TeacherStudentList addStudent(AddStudentRequest request, String teacherUsername) {
        logger.info(String.format("Beginning add student process for: %s.", teacherUsername));
        Teacher teacher = teacherRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        List<Student> students = new ArrayList<>(teacher.getStudents());
        int period = request.getPeriod();
        Set<Role> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByAuthority("STUDENT").orElseThrow());

        Student studentToAdd = new Student(request.getName(), period);
        studentToAdd.setTeacher(teacher);
        studentToAdd.setAuthorities(authorities);

        List<Assignments> studentAssignments = new ArrayList<>();
        studentAssignments.add(assignmentsRepository.save(new Assignments(
                request.getSubject(),
                request.getPeriod(),
                studentToAdd
        )));
        studentToAdd.setAssignments(studentAssignments);

        if (students.contains(studentToAdd)) {
            throw new IllegalArgumentException("Student already exists");
        }

        studentRepository.save(studentToAdd);
        students.add(studentToAdd);
        teacher.setStudents(students);
        teacherRepository.save(teacher);

        return ModelConverter.teacherStudentListToDTO(students, teacher.getSubjects());
    }

    /**
     * Edits an existing student's data
     * @param request request body with all information needed to handle the edit
     * @return an updated List of students
     */
    public PublicStudentDTO editExistingStudent(EditStudentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + request.getStudentId()));
        logger.info(String.format("Editing student with id: %d.", request.getStudentId()));

        if (!request.getNameToChange().isEmpty()) {
            student.setName(request.getNameToChange());
        }
        request.getPeriodToChange().ifPresent(period -> {
            if (!period.equals(student.getPeriod())) {
                student.setPeriod(period);
            }
        });
        studentRepository.save(student);
        Teacher teacher = student.getTeacher();

        return ModelConverter.studentToPublicDTO(student, teacher.getSubjects());
    }
    /**
     * Deletes student using a teacher username and student Id.
     * @param request object containing the id and period of the student.
     * @param teacherUsername unique username
     */
    public void deleteStudent(DeleteStudentRequest request , String teacherUsername) {
        Teacher teacher = teacherRepository.findByUsername(teacherUsername)
                .orElseThrow(NullPointerException::new);
        logger.info(String.format("Deleting student with id: %d.", request.studentId()));

        List<Student> students = teacher.getStudents();
        Optional<Student> studentToDelete = students
                .parallelStream()
                .filter(student -> student.getStudentId().equals(request.studentId()))
                .findFirst();

        studentToDelete.ifPresent(student -> {
            students.remove(student);
            studentRepository.delete(student);
            teacherRepository.save(teacher);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        return studentOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
    }

    private Assignments createNewAssignmentList(String subject, int period,
                                                Student student) {
        return new Assignments(subject, period, student);
    }
}