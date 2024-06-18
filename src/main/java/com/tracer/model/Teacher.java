package com.tracer.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "teachers")
@Data
@EqualsAndHashCode
@ToString(exclude = "students")
@Getter
@Setter
public class Teacher implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "teacher_id")
    private Long teacherId;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @JsonManagedReference
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Student> students;
    private Set<String> subjects;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Teacher_role_junction", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> authorities;
    private boolean isEmailVerified;

    public Teacher() {
    }

    public Teacher(String username , String email , String password , Set<String> subjects , Set<Role> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.subjects = subjects;
        this.students = new ArrayList<>();
        this.authorities = authorities;
        this.isEmailVerified = false;
    }

    public Teacher(String username , String email , String password , Set<Role> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.students = new ArrayList<>();
        this.subjects = new HashSet<>();
        this.authorities = authorities;
        this.isEmailVerified = false;
    }

    public Teacher(String username , String password , Set<Role> authorities) {
        this.username = username;
        this.password = password;
        this.students = new ArrayList<>();
        this.subjects = new HashSet<>();
        this.authorities = authorities;
        this.isEmailVerified = false;
    }

    public Teacher(String username , String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.students = new ArrayList<>();
        this.subjects = new HashSet<>();
        this.isEmailVerified = false;
    }

    public Teacher(String username , String password) {
        this.username = username;
        this.password = password;
        this.students = new ArrayList<>();
        this.subjects = new HashSet<>();
        this.isEmailVerified = false;
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
