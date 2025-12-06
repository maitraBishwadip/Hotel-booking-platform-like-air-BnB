package com.SpringBootProject.AirBnB.strategy;

import com.SpringBootProject.AirBnB.entity.Inventory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service

public class PricingService {

    private final  PricingStrategy pricingStrategy;

    public PricingService(@Qualifier("holidayPrice")PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public BigDecimal calculateDynamicPricing(Inventory inventory) {

        return pricingStrategy.calculatePrice(inventory);
    }
}
