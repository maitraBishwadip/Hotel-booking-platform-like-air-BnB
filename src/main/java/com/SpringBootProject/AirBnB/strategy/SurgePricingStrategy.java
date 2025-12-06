package com.SpringBootProject.AirBnB.strategy;

import com.SpringBootProject.AirBnB.entity.Inventory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("surgePrice")


public class SurgePricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;
    public SurgePricingStrategy(@Qualifier("basePrice")PricingStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        BigDecimal price = wrapped.calculatePrice(inventory);

        return price.multiply(inventory.getSurgeFactor());

    }
}
