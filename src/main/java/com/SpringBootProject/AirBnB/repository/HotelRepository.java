package com.SpringBootProject.AirBnB.repository;


import com.SpringBootProject.AirBnB.entity.Hotel;
import org.apache.logging.log4j.simple.internal.SimpleProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.InterfaceAddress;

@Repository
public interface HotelRepository  extends JpaRepository<Hotel, Long> {



}
