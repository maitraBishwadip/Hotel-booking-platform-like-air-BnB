package com.SpringBootProject.AirBnB.service;

import com.SpringBootProject.AirBnB.dto.RoomDto;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);
    RoomDto getRoomById(Long id);
    List<RoomDto> getAllRooms(Long hotelId);

    void deleteRoomById(Long id);



}
