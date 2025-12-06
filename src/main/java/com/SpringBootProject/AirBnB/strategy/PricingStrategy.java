package com.SpringBootProject.AirBnB.strategy;

import com.SpringBootProject.AirBnB.entity.Inventory;

import java.math.BigDecimal;


public interface PricingStrategy {


    BigDecimal calculatePrice(Inventory inventory);
}
