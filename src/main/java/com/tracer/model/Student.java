package com.tracer.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private Long studentId;
    private String name;
    private Integer period;
    private BigDecimal grade;
}
