package com.SpringBootProject.AirBnB.strategy;

import com.SpringBootProject.AirBnB.entity.Inventory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service("urgencyPrice")


public class UrgencyPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;
    public UrgencyPricingStrategy(@Qualifier("surgePrice")PricingStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        BigDecimal price = wrapped.calculatePrice(inventory);

        LocalDate today = LocalDate.now();

        if(!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7))){
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }


}


