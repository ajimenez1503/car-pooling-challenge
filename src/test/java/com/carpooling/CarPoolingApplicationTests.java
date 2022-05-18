package com.carpooling;

import com.carpooling.controller.CarPoolingController;
import com.carpooling.model.Car;
import com.carpooling.model.Journey;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarPoolingApplicationTests {

    static private List<Car> cars;
    static private List<Journey> journeys;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CarPoolingController controller;

    /**
     * Called before the test.
     * - Initialize the list of cars.
     */
    @BeforeAll
    static void init() {
        cars = new ArrayList<>(4);
        cars.add(new Car(0L, 3));
        cars.add(new Car(1L, 4));
        cars.add(new Car(2L, 5));
        cars.add(new Car(3L, 6));

        journeys = new ArrayList<>(2);
        journeys.add(new Journey(0L, 3));
        journeys.add(new Journey(1L, 2));
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    /**
     * Check that the /status GET API return HttpStatus.OK
     *
     * @throws Exception
     */
    @Test
    public void getStatusAPI() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/status", HttpMethod.GET, entity, String.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Check that the /cars PUT API return HttpStatus.OK and the list of cars passed.
     *
     * @throws Exception
     */
    @Test
    @Order(1)
    public void putCarAPI() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(cars, headers);

        ResponseEntity<List<Car>> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<List<Car>>() {
                });

        assertEquals(cars, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Check that the /cars PUT API return HttpStatus.BAD_REQUEST if passing invalid request body.
     *
     * @throws Exception
     */
    @Test
    public void putCarAPIInvalidRequestBody() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.PUT,
                entity,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Require putCarAPI to be called in advance.
     * Check that the /cars GET API return HttpStatus.OK and the list of cars in the system.
     *
     * @throws Exception
     */
    @Test
    @Order(2)
    public void getCarAPI() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<List<Car>> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Car>>() {
                });

        assertEquals(cars, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Check that the /journey POST API return HttpStatus.ACCEPTED.
     *
     * @throws Exception
     */
    @Test
    @Order(3)
    public void postJourneyAPI() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeys.get(0), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/journey",
                HttpMethod.POST,
                entity,
                String.class);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }


    /**
     * Check that the /journey POST API return HttpStatus.BAD_REQUEST if passing invalid request body.
     *
     * @throws Exception
     */
    @Test
    public void postJourneyAPIInvalidRequestBody() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/journey",
                HttpMethod.POST,
                entity,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
