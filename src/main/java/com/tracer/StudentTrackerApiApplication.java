package com.tracer;

import com.tracer.model.Student;
import com.tracer.model.Teacher;
import com.tracer.repository.StudentRepository;
import com.tracer.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class StudentTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentTrackerApiApplication.class, args);
	}

	// Use this method only when running in dev mode.
	@Bean
	CommandLineRunner run(TeacherRepository teacherRepository, StudentRepository studentRepository) {
		return args -> {
			if (teacherRepository.count() > 0) return;
			Student student = new Student();
			student.setName("jhon");
			student.setGrade(BigDecimal.valueOf(1));
			student.setPeriod(1);

			List<Student> temp = new ArrayList<>();
			temp.add(student);
			Teacher teacher = new Teacher("james", "a", temp);
			teacherRepository.save(teacher);
			studentRepository.save(student);
		};
	}
}
