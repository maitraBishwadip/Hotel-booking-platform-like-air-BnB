package com.SpringBootProject.AirBnB.service;

import com.SpringBootProject.AirBnB.dto.HotelDto;
import com.SpringBootProject.AirBnB.dto.HotelPriceDto;
import com.SpringBootProject.AirBnB.dto.HotelSearchRequest;
import com.SpringBootProject.AirBnB.entity.Hotel;
import com.SpringBootProject.AirBnB.entity.HotelMinPrice;
import com.SpringBootProject.AirBnB.entity.Inventory;
import com.SpringBootProject.AirBnB.entity.Room;
import com.SpringBootProject.AirBnB.repository.HotelMinPriceRepository;
import com.SpringBootProject.AirBnB.repository.InventoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinRepository;

    @Override
    public void initializeRoomForAYear(Room room) {

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        for(;today.isBefore(endDate);today = today.plusDays(1  )){

            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .totalCount(room.getCapacity())
                    .closed(false)
                    .build();

            inventoryRepository.save(inventory);
        }




    }

    @Override
    public void deleteFutureInventory(Room room) {

        LocalDate today = LocalDate.now();

        inventoryRepository.deleteByDateAfterAndRoom(today, room);

      }

  @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
      Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
      Long dateCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;

      //TODO Can change the business logic later if different requirement comes
   Page<HotelPriceDto> hotelPage =
           hotelMinRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity()
    , hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate(), hotelSearchRequest.getRoomCount(),
        dateCount,pageable );




   return hotelPage;

  }
}
