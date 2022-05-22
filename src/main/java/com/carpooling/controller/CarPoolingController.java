package com.carpooling.controller;

import com.carpooling.model.Car;
import com.carpooling.model.CarDTO;
import com.carpooling.model.Journey;
import com.carpooling.model.JourneyDTO;
import com.carpooling.service.CarPoolingService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDTO>> getCars() {
        List<CarDTO> carsDTOResult = modelMapper.map(service.getCars(), new TypeToken<List<CarDTO>>() {
        }.getType());
        return new ResponseEntity<>(carsDTOResult, HttpStatus.OK);
    }

    @GetMapping("/journeys")
    public ResponseEntity<List<JourneyDTO>> getJourney() {
        List<JourneyDTO> journeysDTOResult = modelMapper.map(service.getJourneys(), new TypeToken<List<JourneyDTO>>() {
        }.getType());
        return new ResponseEntity<>(journeysDTOResult, HttpStatus.OK);
    }

    @PutMapping(value = "/cars", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CarDTO>> loadCars(@RequestBody List<CarDTO> carsRequest) {
        List<Car> cars = modelMapper.map(carsRequest, new TypeToken<List<Car>>() {
        }.getType());
        // Remove all previous data
        service.cleanDB();
        List<CarDTO> carsDTOResult = modelMapper.map(service.saveCars(cars), new TypeToken<List<CarDTO>>() {
        }.getType());
        return new ResponseEntity<>(carsDTOResult, HttpStatus.OK);
    }

    @PostMapping(value = "/journey", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> requestJourney(@RequestBody JourneyDTO journeyRequest) {
        Journey journey = modelMapper.map(journeyRequest, Journey.class);
        service.addJourney(journey);
        service.findCarForJourney(journey, true /* newJourney */);
        return ResponseEntity.accepted().build();
    }


    @PostMapping(value = "/dropoff", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> requestDropOff(@RequestBody MultiValueMap<String, String> journeyId) {
        if (!journeyId.containsKey("ID") || journeyId.get("ID").size() > 1) {
            return ResponseEntity.badRequest().build();
        }
        Journey journey = service.getJourneyById(Long.parseLong(journeyId.get("ID").get(0)));
        if (journey != null) {
            service.deleteJourney(journey);
            service.reviewWaitingJourneys();
            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/locate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CarDTO> locate(@RequestBody MultiValueMap<String, String> journeyId) {
        if (!journeyId.containsKey("ID") || journeyId.get("ID").size() > 1) {
            return ResponseEntity.badRequest().build();
        }
        Journey journey = service.getJourneyById(Long.parseLong(journeyId.get("ID").get(0)));
        if (journey != null) {
            if (journey.getCar() != null) {
                return new ResponseEntity<>(modelMapper.map(journey.getCar(), CarDTO.class), HttpStatus.OK);
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
