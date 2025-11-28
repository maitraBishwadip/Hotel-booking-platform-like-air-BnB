package com.SpringBootProject.AirBnB.controllers;


import com.SpringBootProject.AirBnB.dto.HotelDto;
import com.SpringBootProject.AirBnB.repository.HotelRepository;
import com.SpringBootProject.AirBnB.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;


    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto){

      log.info("HotelController createNewHotel attempting a new hotel with name: {}" , hotelDto.getName());

      HotelDto hotel = hotelService.createNewHotel(hotelDto);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(hotel);

    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId){

        log.info("HotelController getHotelById attempting to get hotel with id: {}" , hotelId);
        HotelDto hotel = hotelService.getHotelById(hotelId);
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(hotel);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto)
    {
         HotelDto hotel = hotelService.updateHotelById(hotelId,hotelDto);
         return ResponseEntity
                 .status(HttpStatus.OK)
                 .body(hotel);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId)
    {
         hotelService.deleteHotelById(hotelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<Void> activateHotelId(@PathVariable Long hotelId)
    {
        hotelService.activateHotel(hotelId);
        return ResponseEntity
                .noContent()
                .build();

    }







}
