package com.SpringBootProject.AirBnB.repository;

import com.SpringBootProject.AirBnB.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface guestRepository extends JpaRepository<Guest, Long> {



}
