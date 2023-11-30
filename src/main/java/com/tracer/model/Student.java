package com.tracer.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    @Column(name = "student_id")
    private Long studentId;
    private String name;
    private Integer period;
    private BigDecimal grade;

    public Student() {
    }

    public Student(String name , Integer period , BigDecimal grade) {
        this.name = name;
        this.period = period;
        this.grade = grade;
    }
}
