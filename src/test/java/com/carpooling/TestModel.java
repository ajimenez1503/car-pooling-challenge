package com.carpooling;

import com.carpooling.model.CarDTO;
import com.carpooling.model.JourneyDTO;
import com.carpooling.model.Car;
import com.carpooling.model.Journey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestModel {

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
        assertEquals(car1.hashCode(), car2.hashCode());
    }

    @Test
    void Journey() {
        Journey journey1 = new Journey(0L, 0);
        Journey journey2 = new Journey();
        assertEquals(journey1, journey2);
        assertEquals(journey1.toString(), journey2.toString());
        assertEquals(journey1.hashCode(), journey2.hashCode());
    }

    @Test
    void CarDTO() {
        Car car = new Car(4L, 5);
        CarDTO carDTO1 = this.mapper.map(car, CarDTO.class);
        assertEquals(car.getId(), carDTO1.getId());
        assertEquals(car.getSeats(), carDTO1.getSeats());
        CarDTO carDTO2 = new CarDTO(4L, 5);
        assertEquals(carDTO1, carDTO2);
        assertEquals(carDTO1.toString(), carDTO2.toString());
        assertEquals(carDTO1.hashCode(), carDTO2.hashCode());
    }

    @Test
    void JourneyDTO() {
        Journey journey = new Journey(4L, 5);
        JourneyDTO journeyDTO1 = this.mapper.map(journey, JourneyDTO.class);
        assertEquals(journey.getId(), journeyDTO1.getId());
        assertEquals(journey.getPeople(), journeyDTO1.getPeople());
        JourneyDTO journeyDTO2 = new JourneyDTO(4L, 5);
        assertEquals(journeyDTO1, journeyDTO2);
        assertEquals(journeyDTO1.toString(), journeyDTO2.toString());
        assertEquals(journeyDTO1.hashCode(), journeyDTO2.hashCode());
    }
}