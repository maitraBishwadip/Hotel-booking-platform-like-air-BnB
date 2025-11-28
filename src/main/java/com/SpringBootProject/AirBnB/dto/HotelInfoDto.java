package com.SpringBootProject.AirBnB.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotelInfoDto {



    private HotelDto hotel;

    private List<RoomDto> rooms;

}
