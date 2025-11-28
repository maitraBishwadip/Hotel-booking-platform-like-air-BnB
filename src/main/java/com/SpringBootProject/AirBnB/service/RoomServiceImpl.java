package com.SpringBootProject.AirBnB.service;

import com.SpringBootProject.AirBnB.dto.RoomDto;
import com.SpringBootProject.AirBnB.entity.Hotel;
import com.SpringBootProject.AirBnB.entity.Room;
import com.SpringBootProject.AirBnB.exception.ResourceNotFoundException;
import com.SpringBootProject.AirBnB.repository.HotelRepository;
import com.SpringBootProject.AirBnB.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Template implementation for room-related operations.
 * <p>
 * This class currently provides stubbed methods that log the intent and
 * return placeholder values. Replace TODO sections with real logic
 * (repository calls, mappings, validations) as the domain is finalized.
 * </p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final ModelMapper modelMapper;

    private final HotelRepository hotelRepository;



    private final InventoryService inventoryService;




    /**
     * Create a new room using the provided DTO.
     *
     * @param roomDto the incoming room details
     * @return the created RoomDto once implemented; currently returns null
     */
    @Override
    public RoomDto createNewRoom(Long hotelId ,RoomDto roomDto) {
        log.info("RoomServiceImpl.createNewRoom invoked");

      Hotel hotel = hotelRepository
              .findById(hotelId)
              .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));



            Room room = modelMapper.map(roomDto, Room.class);
            room.setHotel(hotel);
            room = roomRepository.save(room);

            if (hotel.isActive())
            {
                inventoryService.initializeRoomForAYear(room);
            }

            return modelMapper.map(room, RoomDto.class);




    }

    /**
     * Retrieve a room by its identifier.
     *
     * @param id the room id
     * @return the found RoomDto once implemented; currently returns null
     */
    @Override
    public RoomDto getRoomById(Long id) {
        log.info("RoomServiceImpl.getRoomById invoked for id: {}", id);
        // TODO: Fetch Room by id, throw ResourceNotFoundException if absent, map to DTO and return
       Room room = roomRepository
               .findById(id)
               .orElseThrow(()-> new ResourceNotFoundException("Room Not Found with Id" + id));
       return modelMapper.map(room,RoomDto.class);




    }

    /**
     * List all rooms for a given hotel.
     *
     * @param hotelId the hotel id
     * @return list of rooms; currently an empty list as a placeholder
     */
    @Override
    public List<RoomDto> getAllRooms(Long hotelId) {
        log.info("RoomServiceImpl.getAllRooms invoked for hotelId: {}", hotelId);
        // TODO: Query rooms by hotelId, map entities to DTOs and return
      boolean hotelExists = hotelRepository.existsById(hotelId);

      if(!hotelExists)
      {
          throw new ResourceNotFoundException("Hotel not found with id: " + hotelId);
      }


         List<Room>  rooms = roomRepository.findAllByHotelId(hotelId);
           return rooms
                   .stream()
                  .map(room -> modelMapper.map( room, RoomDto.class))
                  .collect(Collectors.toList());

    }

    /**
     * Delete a room by its identifier.
     *
     * @param id the room id
     */
    @Override
    public void deleteRoomById(Long id) {
        log.info("RoomServiceImpl.deleteRoomById invoked for id: {}", id);
        // TODO: Verify existence, handle referential constraints, and delete via repository

        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        roomRepository.deleteById(id);

        inventoryService.deleteFutureInventory(room);
    }
}
