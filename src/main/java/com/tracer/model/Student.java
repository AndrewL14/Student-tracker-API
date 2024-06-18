package com.tracer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tracer.model.assignments.Assignments;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name="students")
@Data
@EqualsAndHashCode
@ToString(exclude = "teacher")
@Getter
@Setter
public class Student implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id", unique = true)
    private Long studentId;
    private String name;
    private String email;
    private String password;
    private Integer period;
    @JsonManagedReference
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Assignments> assignments;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Student_role_junction", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> authorities;

    public Student() {
    }

    public Student(String name , Integer period) {
        this.name = name;
        this.period = period;
        this.assignments = new ArrayList<>();
    }

    public Student(String name , String username , String password , Integer period , Set<Role> authorities) {
        this.name = name;
        this.email = username;
        this.password = password;
        this.period = period;
        this.assignments = new ArrayList<>();
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
