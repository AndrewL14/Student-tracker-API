package com.tracer.model.assignments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "assignment_lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomAssignmentList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Assignment_list_id")
    private Long id;
    private String subject;
    private double averageGrade;
    @OneToMany
    private List<Assignment> assignments;
}
