package com.SpringBootProject.AirBnB.controllers;


import com.SpringBootProject.AirBnB.dto.RoomDto;
import com.SpringBootProject.AirBnB.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom( @PathVariable Long hotelId,
                                                 @RequestBody RoomDto roomDto){

        RoomDto room = roomService.createNewRoom(hotelId,roomDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(room);


    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById( @PathVariable Long roomId){

        roomService.deleteRoomById(roomId);

        return ResponseEntity
                .noContent()
                .build();
        
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms(@PathVariable Long hotelId){

        List<RoomDto> rooms = roomService.getAllRooms(hotelId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rooms);

    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById( @PathVariable Long roomId){
        RoomDto room = roomService.getRoomById(roomId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(room);
    }






}
