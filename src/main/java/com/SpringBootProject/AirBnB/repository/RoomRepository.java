package com.SpringBootProject.AirBnB.repository;

import com.SpringBootProject.AirBnB.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


public List<Room> findAllByHotelId(Long hotelId);


}
