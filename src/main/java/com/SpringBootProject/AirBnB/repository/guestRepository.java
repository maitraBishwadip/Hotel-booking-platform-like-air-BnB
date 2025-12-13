package com.SpringBootProject.AirBnB.repository;

import com.SpringBootProject.AirBnB.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface guestRepository extends JpaRepository<Guest, Long> {



}
