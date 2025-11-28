package com.SpringBootProject.AirBnB.controllers;


import com.SpringBootProject.AirBnB.dto.HotelDto;
import com.SpringBootProject.AirBnB.dto.HotelInfoDto;
import com.SpringBootProject.AirBnB.dto.HotelSearchRequest;
import com.SpringBootProject.AirBnB.service.HotelService;
import com.SpringBootProject.AirBnB.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {


    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestParam HotelSearchRequest hotelSearchRequest)
    {

        Page<HotelDto> page =  inventoryService.searchHotels(hotelSearchRequest);

        return ResponseEntity
                .status(200)
                .body(page);


    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long  hotelId){





  return ResponseEntity
          .ok(hotelService.getHotelInfoById(hotelId));


    }


}
