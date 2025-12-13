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
      log.info("Starting price update job");
      int page = 0 ;
      int batchSize = 100;

      while(true)
      {
          log.info("Processing page: {}", page);
          Page<Hotel> hotelPage = hotelrepository.findAll(PageRequest.of(page, batchSize));
          if(hotelPage.isEmpty()){
              log.info("No more hotels to process. Exiting.");
              break;
          }
          hotelPage.forEach(this::updateHotelPrices);

         page++;
      }
      log.info("Finished price update job");

    }

    private void updateHotelPrices(Hotel hotel)
    {
        log.info("Updating prices for hotel: {}", hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

       List<Inventory> inventoryList = inventoryRepository.findByHotelDateBetween(hotel, startDate, endDate);
        log.info("Found {} inventory items to update for hotel: {}", inventoryList.size(), hotel.getId());

       updateInventoryPrices(inventoryList);
       updateHotelMinPrice(hotel,inventoryList,startDate,endDate);
       log.info("Finished updating prices for hotel: {}", hotel.getId());


    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        log.info("Updating hotel min price for hotel: {}", hotel.getId());
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                   Inventory::getDate,
                  Collectors.mapping(Inventory::getPrice,Collectors.minBy(Comparator.naturalOrder()))
                  ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));
        log.info("Calculated {} daily min prices for hotel: {}", dailyMinPrices.size(), hotel.getId());
List<HotelMinPrice> hotelPrices = new ArrayList<>();
dailyMinPrices.forEach((date,price) -> { HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date)
.orElse(new HotelMinPrice(hotel,date));
hotelPrice.setPrice(price);
hotelPrices.add(hotelPrice);
});
hotelMinPriceRepository.saveAll(hotelPrices);
log.info("Saved {} hotel min prices for hotel: {}", hotelPrices.size(), hotel.getId());
    }

    private void updateInventoryPrices(List<Inventory> inventoryList) {
        log.info("Updating {} inventory items", inventoryList.size());
        inventoryList.forEach(inventory->{
            log.debug("Updating inventory item: {}", inventory.getId());
            BigDecimal dynamicPrice = pricingservice.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
            inventoryRepository.save(inventory);
            log.debug("Saved inventory item: {}", inventory.getId());
        });
        log.info("Finished updating inventory items");
    }
}
