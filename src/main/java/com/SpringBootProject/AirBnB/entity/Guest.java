package com.SpringBootProject.AirBnB.entity;


import com.SpringBootProject.AirBnB.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter

public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;






}
