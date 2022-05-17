package com.carpooling.repository;

import com.carpooling.model.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findCarByAvailableSeatsGreaterThanEqualOrderByAvailableSeats(int availableSeats);
}
