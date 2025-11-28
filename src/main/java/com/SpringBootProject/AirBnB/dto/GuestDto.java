package com.SpringBootProject.AirBnB.dto;

import com.SpringBootProject.AirBnB.entity.User;
import com.SpringBootProject.AirBnB.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;


    private User user;

    private String name;



    private Gender gender;

    private Integer age;



}
