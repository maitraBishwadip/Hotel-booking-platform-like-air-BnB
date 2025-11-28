package com.SpringBootProject.AirBnB.entity;


import com.SpringBootProject.AirBnB.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import org.springframework.context.support.BeanDefinitionDsl;

import java.util.Set;

@Entity
@Getter
@Setter

@Table(name = "app_user")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private String email;


    @Column(nullable = false)
    private String password; //TODO Encoded password using BCrypt


    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;





}
