package com.carpooling;

import com.carpooling.controller.CarPoolingController;
import com.carpooling.model.Car;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
class CarPoolingApplicationTests {

    static private List<Car> cars;
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
        cars = new ArrayList<>(2);
        cars.add(new Car(1L, 3));
        cars.add(new Car(2L, 4));
        cars.add(new Car(3L, 5));
        cars.add(new Car(4L, 6));
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
     * Check that the /cars PUT API return HttpStatus.BAD_REQUEST if passing invalid body.
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
}
