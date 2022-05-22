package com.carpooling;

import com.carpooling.dto.CarDTO;
import com.carpooling.dto.JourneyDTO;
import com.carpooling.model.Car;
import com.carpooling.model.Journey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestModel {

    private  ModelMapper mapper;

    @BeforeEach
    public void setup() {
        this.mapper = new ModelMapper();
    }

    @Test
    void Car() {
        Car car1 = new Car(0L, 0);
        Car car2 = new Car();
        assertEquals(car1, car2);
        assertEquals(car1.toString(), car2.toString());
    }

    @Test
    void Journey() {
        Journey journey1 = new Journey(0L, 0);
        Journey journey2 = new Journey();
        assertEquals(journey1, journey2);
        assertEquals(journey1.toString(), journey2.toString());
    }

    @Test
    void CarDTO() {
        Car car = new Car(4L, 5);
        CarDTO carDTO = this.mapper.map(car, CarDTO.class);
        assertEquals(car.getId(), carDTO.getId());
        assertEquals(car.getSeats(), carDTO.getSeats());
    }

    @Test
    void JourneyDTO() {
        Journey journey = new Journey(4L, 5);
        JourneyDTO journeyDTO = this.mapper.map(journey, JourneyDTO.class);
        assertEquals(journey.getId(), journeyDTO.getId());
        assertEquals(journey.getPeople(), journeyDTO.getPeople());
    }
}