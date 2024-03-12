package com.tracer.model;

import com.tracer.model.assignments.Assignment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="students")
@Data
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id", unique = true)
    private Long studentId;
    private String name;
    private Integer period;
    private BigDecimal grade;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    public Student() {
    }

    public Student(String name , Integer period , BigDecimal grade) {
        this.name = name;
        this.period = period;
        this.grade = grade;
    }
}
