package com.SpringBootProject.AirBnB.strategy;

import com.SpringBootProject.AirBnB.entity.Inventory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("holidayPrice")

public class HolidayPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;
    public HolidayPricingStrategy(@Qualifier("urgencyPrice")PricingStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean isHoliday = false;

        if(isHoliday)
        {
            price = price.multiply(BigDecimal.valueOf(1.252));
        }
        return price;
    }

}
