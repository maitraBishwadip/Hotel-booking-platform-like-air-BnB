package com.SpringBootProject.AirBnB.strategy;

import com.SpringBootProject.AirBnB.entity.Inventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("basePrice")
@RequiredArgsConstructor
@Getter
@Setter

public class BasePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        return inventory.getRoom().getBasePrice();

    }



}
