package com.SpringBootProject.AirBnB.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter

@Table(name = "inventory",
        uniqueConstraints = @UniqueConstraint(
                name= "unique_hotel_room_date",
                columnNames = {"hotel_id", "room_id", "date"}

        )) //Later I will add indexes for this table as well

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="room_id",nullable=false)
    private Room room;

    @Column(nullable = false)
    private LocalDate date;


    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer reservedCount;

    @Column(nullable = false)
    private Integer totalCount;


    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal surgeFactor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;  //Base Price * Surge Factor


    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Boolean closed; // We will use this to check if the room is available or not and won't show if the room is closed


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate updatedAt;



}
