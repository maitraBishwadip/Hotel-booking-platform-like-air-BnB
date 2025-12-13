package com.SpringBootProject.AirBnB.service;

import com.SpringBootProject.AirBnB.dto.HotelDto;
import com.SpringBootProject.AirBnB.dto.HotelPriceDto;
import com.SpringBootProject.AirBnB.dto.HotelSearchRequest;
import com.SpringBootProject.AirBnB.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteFutureInventory(Room room);


    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
