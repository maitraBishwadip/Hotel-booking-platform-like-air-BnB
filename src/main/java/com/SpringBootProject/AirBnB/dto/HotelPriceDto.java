package com.SpringBootProject.AirBnB.dto;

import com.SpringBootProject.AirBnB.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor


public class HotelPriceDto {


    private Hotel hotel;
    private Double price;

}
