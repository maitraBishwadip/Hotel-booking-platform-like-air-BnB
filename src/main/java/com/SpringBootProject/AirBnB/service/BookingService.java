package com.SpringBootProject.AirBnB.service;


import com.SpringBootProject.AirBnB.dto.BookingDto;
import com.SpringBootProject.AirBnB.dto.BookingRequest;
import org.springframework.stereotype.Service;






public interface BookingService {



    BookingDto initializeBooking(BookingRequest bookingRequest);
}
