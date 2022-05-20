package com.carpooling.controller;

import com.carpooling.model.Car;
import com.carpooling.model.Journey;
import com.carpooling.service.CarPoolingService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CarPoolingController {

    @Autowired
    private CarPoolingService service;

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getCars() {
        return new ResponseEntity<>(service.getCars(), HttpStatus.OK);
    }

    @GetMapping("/journeys")
    public ResponseEntity<List<Journey>> getJourney() {
        return new ResponseEntity<>(service.getJourney(), HttpStatus.OK);
    }

    @PutMapping(value = "/cars", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Car>> loadCars(@RequestBody List<Car> cars) {
        // Remove all previous data
        service.cleanDB();
        return new ResponseEntity<>(service.saveCars(cars), HttpStatus.OK);
    }

    @PostMapping(value = "/journey", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> requestJourney(@RequestBody Journey newJourney) {
        service.addJourney(newJourney);
        service.findCarForJourney(newJourney, true /* newJourney */);
        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }


    @PostMapping(value = "/dropoff", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> requestDropOff(@RequestBody MultiValueMap<String, String> journeyId) {
        if (!journeyId.containsKey("ID") || journeyId.get("ID").size() > 1) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        Journey journey = service.getJourneyById(Long.parseLong(journeyId.get("ID").get(0)));
        if (journey != null) {
            service.deleteJourney(journey);
            service.reviewWaitingJourneys();
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/locate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<@Nullable Car> locate(@RequestBody MultiValueMap<String, String> journeyId) {
        if (!journeyId.containsKey("ID") || journeyId.get("ID").size() > 1) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Journey journey = service.getJourneyById(Long.parseLong(journeyId.get("ID").get(0)));
        if (journey != null) {
            return new ResponseEntity<>(
                    journey.getCar(),
                    journey.getCar() != null
                            ? HttpStatus.OK
                            : HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
