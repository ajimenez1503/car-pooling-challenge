package com.carpooling.service;

import com.carpooling.model.Car;
import com.carpooling.model.Journey;
import com.carpooling.repository.CarRepository;
import com.carpooling.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarPoolingService {

    // The access to the list has to be thread safe.
    private final List<Journey> waitingJourneys = Collections.synchronizedList(new ArrayList());
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
        return journey.orElse(null);
    }


    private void updateCar(Car car, int removeAvailableSeats, int addAvailableSeats) {
        car.updateAvailableSeats(car.getAvailableSeats() - removeAvailableSeats + addAvailableSeats);
        carRepository.save(car);
    }

    // Concurrency: It is not possible to delete a journey and find a car for it at the same time.
    private synchronized boolean findCarForJourney(Journey journey) {
        List<Car> cars = carRepository.findCarByAvailableSeatsGreaterThanEqualOrderByAvailableSeats(journey.getPeople());
        if (!cars.isEmpty()) {
            updateCar(cars.get(0), journey.getPeople(), 0);
            journey.setCar(cars.get(0));
            journeyRepository.save(journey);
            return true;
        }
        return false;
    }

    // Concurrency: It is not possible to delete a journey and find a car for it at the same time.
    public synchronized void deleteJourney(Journey journey) {
        int seatsUsed = journey.getPeople();
        Car car = journey.getCar();
        journeyRepository.delete(journey);
        if (car != null) {
            updateCar(car, 0, seatsUsed);
        } else {
            waitingJourneys.remove(journey);
        }
    }

    public void reviewWaitingJourneys() {
        // Use iterator to remove inside of the loop.
        Iterator<Journey> itr = waitingJourneys.iterator();
        Journey journey;
        while (itr.hasNext()) {
            journey = itr.next();
            if (findCarForJourney(journey)) {
                itr.remove();
            }
        }
    }

    public void registerJourney(Journey journey) {
        if (!findCarForJourney(journey)) {
            waitingJourneys.add(journey);
        }
    }
}
