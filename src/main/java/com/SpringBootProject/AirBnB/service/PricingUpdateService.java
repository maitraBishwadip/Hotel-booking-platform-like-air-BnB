package com.SpringBootProject.AirBnB.service;

import com.SpringBootProject.AirBnB.entity.Hotel;
import com.SpringBootProject.AirBnB.entity.HotelMinPrice;
import com.SpringBootProject.AirBnB.entity.Inventory;
import com.SpringBootProject.AirBnB.repository.HotelMinPriceRepository;
import com.SpringBootProject.AirBnB.repository.HotelRepository;
import com.SpringBootProject.AirBnB.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class PricingUpdateService {


    //Schedular to update the inventory and HotelMinPrice tables every hour
   private final HotelRepository hotelrepository;
   private final InventoryRepository inventoryRepository;
   private final HotelMinPriceRepository hotelMinPriceRepository;
   private final PricingService pricingservice;


@Scheduled(cron = "0 0 * * * *")
    public void updatePrices()
    {
      int page = 0 ;
      int batchSize = 100;

      while(true)
      {
          Page<Hotel> hotelPage = hotelrepository.findAll(PageRequest.of(page, batchSize));
          if(hotelPage.isEmpty())
              break;
          hotelPage.forEach(this::updateHotelPrices);

         page++;
      }

    }

    private void updateHotelPrices(Hotel hotel)
    {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

       List<Inventory> inventoryList = inventoryRepository.findByHotelDateBetween(hotel, startDate, endDate);

       updateInventoryPrices(inventoryList);
       updateHotelMinPrice(hotel,inventoryList,startDate,endDate);


    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                   Inventory::getDate,
                  Collectors.mapping(Inventory::getPrice,Collectors.minBy(Comparator.naturalOrder()))
                  ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));
List<HotelMinPrice> hotelPrices = new ArrayList<>();
dailyMinPrices.forEach((date,price) -> { HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date)
.orElse(new HotelMinPrice(hotel,date));
hotelPrice.setPrice(price);
hotelPrices.add(hotelPrice);
});
hotelMinPriceRepository.saveAll(hotelPrices);
    }

    private void updateInventoryPrices(List<Inventory> inventoryList) {
        inventoryList.forEach(inventory->{
            BigDecimal dynamicPrice = pricingservice.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
            inventoryRepository.save(inventory);
        });
    }
}


















