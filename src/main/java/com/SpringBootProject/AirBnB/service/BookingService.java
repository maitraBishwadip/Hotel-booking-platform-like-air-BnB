package com.SpringBootProject.AirBnB.service;


import com.SpringBootProject.AirBnB.dto.BookingDto;
import com.SpringBootProject.AirBnB.dto.BookingRequest;
import com.SpringBootProject.AirBnB.dto.GuestDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BookingService {



    BookingDto initializeBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);


}
