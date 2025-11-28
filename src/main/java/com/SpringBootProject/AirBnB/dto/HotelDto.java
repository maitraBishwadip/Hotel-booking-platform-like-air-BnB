package com.SpringBootProject.AirBnB.dto;

import com.SpringBootProject.AirBnB.entity.HotelContactInfo;
import jakarta.persistence.Column;
import lombok.Data;


@Data
public class HotelDto {


    private String name;

    private String  city;

    private HotelContactInfo hotelContactinfo;



    private String[] amenities;
    private String[] photos;







    private boolean Active;

}
