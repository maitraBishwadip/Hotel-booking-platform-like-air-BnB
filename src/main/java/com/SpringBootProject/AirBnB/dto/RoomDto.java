package com.SpringBootProject.AirBnB.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.SpringBootProject.AirBnB.entity.Room}
 */
@Data

@RequiredArgsConstructor

public class RoomDto  {

    private  String type;
    private  BigDecimal basePrice;
    private  String[] photos;
    private  String[] amenities;
    private  Integer totalCount;
    private Integer capacity;
    private Boolean active = true;

}