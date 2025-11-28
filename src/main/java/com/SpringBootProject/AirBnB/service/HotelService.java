package com.SpringBootProject.AirBnB.service;

import com.SpringBootProject.AirBnB.dto.HotelDto;
import com.SpringBootProject.AirBnB.dto.HotelInfoDto;

public interface HotelService {


    HotelDto createNewHotel(HotelDto hotelDto );

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

     void activateHotel(Long hotelId);


    HotelInfoDto getHotelInfoById(Long hotelId);
}
