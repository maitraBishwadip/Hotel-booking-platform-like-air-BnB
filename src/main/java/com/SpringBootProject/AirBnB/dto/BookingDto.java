package com.SpringBootProject.AirBnB.dto;

import com.SpringBootProject.AirBnB.entity.Guest;
import com.SpringBootProject.AirBnB.entity.Hotel;
import com.SpringBootProject.AirBnB.entity.Room;
import com.SpringBootProject.AirBnB.entity.User;
import com.SpringBootProject.AirBnB.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;


@Data
public class BookingDto {





    private Long id;





    private Integer roomsCount;




    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    private LocalDateTime checkInDate;

    private LocalDateTime checkOutDate;





    private BookingStatus bookingStatus;



    private Set<Guest> guests;


}
