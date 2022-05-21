package com.carpooling.controller;

import com.carpooling.dto.CarDTO;
import com.carpooling.dto.JourneyDTO;
import com.carpooling.model.Car;
import com.carpooling.model.Journey;
import com.carpooling.service.CarPoolingService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import java.lang.reflect.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CarPoolingController {

    @Autowired
    private CarPoolingService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDTO>> getCars() {
        List<CarDTO> carsDTOResult = modelMapper.map(service.getCars(), new TypeToken<List<CarDTO>>() {}.getType());
        return new ResponseEntity<>(carsDTOResult, HttpStatus.OK);
    }

    @GetMapping("/journeys")
    public ResponseEntity<List<Journey>> getJourney() {
        return new ResponseEntity<>(service.getJourney(), HttpStatus.OK);
    }

    @PutMapping(value = "/cars", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CarDTO>> loadCars(@RequestBody List<CarDTO> carsRequest) {
        List<Car> cars = modelMapper.map(carsRequest, new TypeToken<List<Car>>() {}.getType());
        // Remove all previous data
        service.cleanDB();
        List<CarDTO> carsDTOResult = modelMapper.map(service.saveCars(cars), new TypeToken<List<CarDTO>>() {}.getType());
        return new ResponseEntity<>(carsDTOResult, HttpStatus.OK);
    }

    @PostMapping(value = "/journey", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> requestJourney(@RequestBody JourneyDTO journeyRequest) {
        Journey journey = modelMapper.map(journeyRequest, Journey.class);
        service.addJourney(journey);
        service.findCarForJourney(journey, true /* newJourney */);
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
    public ResponseEntity<CarDTO> locate(@RequestBody MultiValueMap<String, String> journeyId) {
        if (!journeyId.containsKey("ID") || journeyId.get("ID").size() > 1) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Journey journey = service.getJourneyById(Long.parseLong(journeyId.get("ID").get(0)));
        if (journey != null) {
            if (journey.getCar() != null) {
                return new ResponseEntity<>(modelMapper.map(journey.getCar(), CarDTO.class), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
