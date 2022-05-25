package com.carpooling;

import com.carpooling.model.CarDTO;
import com.carpooling.model.JourneyDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
 * Let's start our testing adventure with a test class that uses MockMvc. In this approach, the Spring Boot application server will not be started but the code will be called exactly in the same way as if it handling an HTTP request.
 * Ref: https://frontbackend.com/spring-boot/spring-boot-2-junit-5-mockito
 */

@SpringBootTest
@AutoConfigureMockMvc
class TestMockController {

    static private List<CarDTO> cars;
    static private List<JourneyDTO> journeys;
    @Autowired
    private MockMvc mockMvc;

    /**
     * Called before the test.
     * - Initialize the list of cars.
     */
    @BeforeAll
    static void init() {
        cars = new ArrayList<>(4);
        cars.add(new CarDTO(0L, 2));
        cars.add(new CarDTO(1L, 5));

        journeys = new ArrayList<>(2);
        journeys.add(new JourneyDTO(0L, 4));
        journeys.add(new JourneyDTO(1L, 3));
    }

    @Test
    public void integrationTest() throws Exception {
        // Check status is working
        this.mockMvc.perform(get("/status")).andDo(print())
                .andExpect(status().isOk());

        // Add a list of cars by the PUT /cars API
        this.mockMvc.perform(put("/cars").contentType(MediaType.APPLICATION_JSON).content(cars.toString()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().bytes(cars.toString().replaceAll(" ", "").getBytes()),
                        content().json(cars.toString()));

        // Check the list of cars by the GET /cars API
        this.mockMvc.perform(get("/cars").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().bytes(cars.toString().replaceAll(" ", "").getBytes()),
                        content().json(cars.toString()));

        // Add a journey by the POST /journey API
        this.mockMvc.perform(post("/journey").contentType(MediaType.APPLICATION_JSON).content(journeys.get(0).toString()))
                .andDo(print())
                .andExpect(status().isAccepted());

        // Check the journey by the GET /journeys API
        List<JourneyDTO> resultJourney = new ArrayList<>();
        resultJourney.add(journeys.get(0));
        this.mockMvc.perform(get("/journeys").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().bytes(resultJourney.toString().replaceAll(" ", "").getBytes()),
                        content().json(resultJourney.toString()));

        // Check that the first journey has a car assigned by the POST /locate API
        this.mockMvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("ID=" + journeys.get(0).getId()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().bytes(cars.get(1).toString().replaceAll(" ", "").getBytes()),
                        content().json(cars.get(1).toString()));

        // Add the other journey by the POST /journey API
        this.mockMvc.perform(post("/journey").contentType(MediaType.APPLICATION_JSON).content(journeys.get(1).toString()))
                .andDo(print())
                .andExpect(status().isAccepted());

        // Check the journeys by the GET /journeys API
        this.mockMvc.perform(get("/journeys").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().bytes(journeys.toString().replaceAll(" ", "").getBytes()),
                        content().json(journeys.toString()));

        // Check the journey does not have a car by the POST /locate API
        this.mockMvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("ID=" + journeys.get(1).getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Delete the first journey by the POST /dropoff API
        this.mockMvc.perform(post("/dropoff").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("ID=" + journeys.get(0).getId()))
                .andDo(print())
                .andExpect(status().isOk());

        // Check that the second journey has a car assigned by the POST /locate API
        this.mockMvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("ID=" + journeys.get(1).getId()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().bytes(cars.get(1).toString().replaceAll(" ", "").getBytes()),
                        content().json(cars.get(1).toString()));

        // Delete the second journey by the POST /dropoff API
        this.mockMvc.perform(post("/dropoff").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("ID=" + journeys.get(1).getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
