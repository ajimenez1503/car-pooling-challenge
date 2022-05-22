package com.carpooling.service;

import com.carpooling.model.Car;
import com.carpooling.model.Journey;
import com.carpooling.repository.CarRepository;
import com.carpooling.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarPoolingService {

    private final List<Journey> waitingJourneys = new ArrayList<>();
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private JourneyRepository journeyRepository;

    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public List<Journey> getJourneys() {
        return journeyRepository.findAll();
    }


    public void cleanDB() {
        carRepository.deleteAll();
        journeyRepository.deleteAll();
        waitingJourneys.clear();
    }

    public List<Car> saveCars(List<Car> cars) {
        return carRepository.saveAll(cars);
    }

    public void addJourney(Journey newJourney) {
        journeyRepository.save(newJourney);
    }

    public Journey getJourneyById(Long journeyId) {
        Optional<Journey> journey = journeyRepository.findById(journeyId);
        if (journey.isPresent()) {
            return journey.get();
        } else {
            return null;
        }
    }

    public void deleteJourney(Journey journey) {
        int seatsUsed = journey.getPeople();
        Car car = journey.getCar();
        journeyRepository.delete(journey);
        if (car != null) {
            car.updateAvailableSeats(car.getAvailableSeats() + seatsUsed);
            carRepository.save(car);
        } else {
            waitingJourneys.remove(journey);
        }
    }

    // @Async
    public void reviewWaitingJourneys() {
        for (Journey journey : waitingJourneys) {
            if (findCarForJourney(journey, false /* newJourney */)) {
                waitingJourneys.remove(journey);
                return;
            }
        }
    }

    // @Async
    public boolean findCarForJourney(Journey journey, boolean newJourney) {
        List<Car> cars = carRepository.findCarByAvailableSeatsGreaterThanEqualOrderByAvailableSeats(journey.getPeople());
        if (!cars.isEmpty()) {
            Car car = cars.get(0);
            car.updateAvailableSeats(car.getAvailableSeats() - journey.getPeople());
            carRepository.save(car);
            journey.setCar(car);
            journeyRepository.save(journey);
            return true;
        } else if (newJourney) {
            waitingJourneys.add(journey);
        }
        return false;
    }
}
