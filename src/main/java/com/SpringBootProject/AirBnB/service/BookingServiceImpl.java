package com.SpringBootProject.AirBnB.service;


import com.SpringBootProject.AirBnB.dto.BookingDto;
import com.SpringBootProject.AirBnB.dto.BookingRequest;
import com.SpringBootProject.AirBnB.entity.*;
import com.SpringBootProject.AirBnB.entity.enums.BookingStatus;
import com.SpringBootProject.AirBnB.exception.ResourceNotFoundException;
import com.SpringBootProject.AirBnB.repository.BookingRepository;
import com.SpringBootProject.AirBnB.repository.HotelRepository;
import com.SpringBootProject.AirBnB.repository.InventoryRepository;
import com.SpringBootProject.AirBnB.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@Service
@RequiredArgsConstructor
@Slf4j




public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private ModelMapper modelMapper;




    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequest bookingRequest)
    {
        log.info("BookingServiceImpl.initializeBooking - Initializing booking");
        // Implementation logic to initialize booking goes here

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId()).
                orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(
                room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount()
        );

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())+1;

        if(inventoryList.size() != daysCount)
        {
            log.error("BookingServiceImpl.initializeBooking - Not enough inventory available for booking");
            throw new RuntimeException("Not enough inventory available for booking"); //TODO Later I will create Illegal StateException and throw that
        }



        // Resurve the Inventory
        for (Inventory inventory : inventoryList)
        {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());

        }
        inventoryRepository.saveAll(inventoryList);

        // Create The Booking Entity and save it to the database

        //TODO Remote the temporary User after authorized User is created
        User user = new User();
        user.setId(1L); // Temporary User ID


        //TODO : Calculate Dynamic Price and set it to Booking Later on


        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .roomsCount(bookingRequest.getRoomsCount())
                .user(user) //TODO Change with Spring Sequrity
                .checkInDate(bookingRequest.getCheckInDate().atStartOfDay())
                .checkOutDate(bookingRequest.getCheckOutDate().atStartOfDay())
                .bookingStatus(BookingStatus.RESERVED)
                .amount(BigDecimal.TEN)
                .build();

        booking=  bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);
         // Replace with actual BookingDto after implementation


    }



}
