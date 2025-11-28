package com.SpringBootProject.AirBnB.entity;


import com.SpringBootProject.AirBnB.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true ,nullable  = false)
    private String transitionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;


    @OneToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

}
