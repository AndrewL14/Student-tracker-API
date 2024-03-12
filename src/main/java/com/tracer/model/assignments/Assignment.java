package com.tracer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assignments")
@Data
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assignment_id")
    private Long assignmentId;
    private String assignmentName;
    private double grade;
    private String assignmentType;
}
