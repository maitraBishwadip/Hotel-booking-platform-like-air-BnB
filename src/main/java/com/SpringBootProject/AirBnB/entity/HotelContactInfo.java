package com.SpringBootProject.AirBnB.entity;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable  //This will be used to embed the data in the entity
public class HotelContactInfo {

    private String address;
    private String email;
    private String phoneNumber;
    private String location;

}
