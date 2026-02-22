package com.SpringBootProject.AirBnB.service;

import com.SpringBootProject.AirBnB.dto.HotelDto;
import com.SpringBootProject.AirBnB.dto.HotelInfoDto;
import com.SpringBootProject.AirBnB.dto.RoomDto;
import com.SpringBootProject.AirBnB.entity.Hotel;
import com.SpringBootProject.AirBnB.entity.Room;
import com.SpringBootProject.AirBnB.exception.ResourceNotFoundException;
import com.SpringBootProject.AirBnB.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("HotelServiceImpl.createNewHotel");
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        User user = (SecurityProperties.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user); //Todo: import the db user
        hotel = hotelRepository.save(hotel);
        log.info("HotelServiceImpl.createNewHotel - Hotel created with id: " + hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        hotel.setId(id);
        modelMapper.map(hotelDto, hotel);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        for(Room room : hotel.getRooms()) {
            inventoryService.deleteFutureInventory(room);
        }

        hotelRepository.delete(hotel);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        hotel.setActive(true);

        for(Room room : hotel.getRooms()) {
            inventoryService.initializeRoomForAYear(room);
        }

        hotelRepository.save(hotel);
        log.info("HotelServiceImpl.activateHotel - Hotel with id: " + hotelId + " activated");
    }


    @Override
 public HotelInfoDto getHotelInfoById(Long hotelId)
    {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .toList();


         HotelInfoDto hotelInfoDto = modelMapper.map(hotel, HotelInfoDto.class);
         hotelInfoDto.setRooms(rooms);
         return hotelInfoDto;
    }
}
