package com.SpringBootProject.AirBnB.dto;


import com.SpringBootProject.AirBnB.entity.Hotel;
import com.SpringBootProject.AirBnB.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InventoryDto {





    private Integer bookedCount;



    private Integer totalCount;



    private BigDecimal surgeFactor;


    private BigDecimal price;  //Base Price * Surge Factor



    private String city;


    private Boolean closed; // We will use this to check if the room is available or not and won't show if the room is closed






}
