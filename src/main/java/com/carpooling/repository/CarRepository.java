package com.carpooling.repository;

import com.carpooling.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findCarByAvailableSeatsGreaterThanEqualOrderByAvailableSeats(int availableSeats);
}
