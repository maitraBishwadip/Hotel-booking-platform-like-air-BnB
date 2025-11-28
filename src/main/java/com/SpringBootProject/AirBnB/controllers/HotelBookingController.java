package com.SpringBootProject.AirBnB.controllers;


import com.SpringBootProject.AirBnB.dto.BookingDto;
import com.SpringBootProject.AirBnB.dto.BookingRequest;
import com.SpringBootProject.AirBnB.entity.Booking;
import com.SpringBootProject.AirBnB.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class HotelBookingController {


    private final BookingService bookingService;

@PostMapping(value = "/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingrequest){

   return ResponseEntity.
           status(200)
           .body(bookingService.initializeBooking(bookingrequest));








    }



}
