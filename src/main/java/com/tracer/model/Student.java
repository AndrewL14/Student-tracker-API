package com.tracer.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="students")
@Data
@EqualsAndHashCode
@ToString
@Builder
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
}
