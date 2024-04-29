package com.tracer;

import com.tracer.model.Role;
import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.model.assignments.Assignment;
import com.tracer.model.assignments.AssignmentType;
import com.tracer.repository.AuthorityRepository;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableScheduling
public class StudentTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentTrackerApiApplication.class, args);
	}

	// Use this method only when running in dev mode.
	@Bean
	CommandLineRunner run(TeacherRepository teacherRepository, StudentRepository studentRepository,
						  PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
		return args -> {
			if (teacherRepository.count() > 0) return;
			Role role = authorityRepository.save(new Role("TEACHER"));
			authorityRepository.save(new Role("test"));
			Set<Role> authorities = new HashSet<>();
			authorities.add(role);

			Assignment assignment = new Assignment("Adding and subtracting", 100.0,
					true, false, LocalDate.now(), AssignmentType.ASSIGNMENT.toString());
			Student student = new Student();
			student.setName("Jhon Smith");
			student.setGrade(BigDecimal.valueOf(100));
			student.setAssignments(List.of(assignment));
			student.setPeriod(1);

			List<Student> temp = new ArrayList<>();
			temp.add(student);
			Teacher teacher = new Teacher("james", passwordEncoder.encode("password") , "example@gmail.com", temp);
			teacher.setAuthorities(authorities);
			teacherRepository.save(teacher);
			studentRepository.save(student);
		};
	}
}
