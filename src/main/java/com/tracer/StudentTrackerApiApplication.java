package com.tracer;

import com.tracer.model.Role;
import com.tracer.model.users.Student;
import com.tracer.model.users.Teacher;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.Assignments;
import com.tracer.model.assignments.AssignmentType;
import com.tracer.repository.assignments.AssignmentRepository;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import com.tracer.repository.assignments.AssignmentsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class StudentTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentTrackerApiApplication.class, args);
	}

	// Use this method only when running in dev mode.
	@Bean
	CommandLineRunner run(TeacherRepository teacherRepository, StudentRepository studentRepository,
						  PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository,
						  AssignmentRepository assignmentRepository, AssignmentsRepository assignmentsRepository) {
		return args -> {
			if (teacherRepository.count() > 0) return;

			// Create and save roles
			Role role = authorityRepository.save(new Role("TEACHER"));
			Role studentRole = authorityRepository.save(new Role("STUDENT"));
			Set<Role> authorities = new HashSet<>();
			authorities.add(role);
			Set<Role> studentAuthorities = new HashSet<>();
			studentAuthorities.add(studentRole);

			// Create Subjects
			Set<String> subjects = new HashSet<>();
			subjects.add("math");

			// Create and save teacher
			Teacher teacher = new Teacher("James", "example@gmail.com",
					passwordEncoder.encode("password"), authorities);
			teacher.setSubjects(subjects);
			teacherRepository.save(teacher);

			// Create and save student
			Student student = new Student("Jhon Smith", "john@example.com",
					passwordEncoder.encode("test"), 1, studentAuthorities);
			student.setTeacher(teacher);
			studentRepository.save(student);

			// Set students to the teacher and update the teacher in the repository
			teacher.setStudents(List.of(student));
			teacherRepository.save(teacher);

			// Create assignment list for the student
			Assignments studentAssignments = new Assignments("math", 1, student);

			// Save the assignments object
			assignmentsRepository.save(studentAssignments);

			// Create and save an assignment
			Assignment assignment = new Assignment("Adding and subtracting", 100.0,
					true, false, LocalDate.now(), AssignmentType.ASSIGNMENT.toString(), studentAssignments);
			assignmentRepository.save(assignment);

			studentAssignments.setAssignments(List.of(assignment));
			student.setAssignments(List.of(studentAssignments));
			assignmentsRepository.save(studentAssignments);
			studentRepository.save(student);
			teacherRepository.save(teacher);

		};
	}

}
