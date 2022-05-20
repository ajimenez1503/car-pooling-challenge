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
import org.springframework.util.LinkedMultiValueMap;

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
        cars.add(new Car(0L, 2));
        cars.add(new Car(1L, 5));

        journeys = new ArrayList<>(2);
        journeys.add(new Journey(0L, 4));
        journeys.add(new Journey(1L, 3));
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
    public void getStatusApi() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/status", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /cars PUT API return HttpStatus.OK and the list of cars passed.
     *
     * @throws Exception
     */
    @Test
    @Order(1)
    public void putCarApi() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(cars, headers);

        ResponseEntity<List<Car>> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<List<Car>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars, response.getBody());
    }

    /**
     * Check that the /cars PUT API return HttpStatus.BAD_REQUEST if passing invalid request body.
     *
     * @throws Exception
     */
    @Test
    public void putCarApiInvalidRequestBody() throws Exception {
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
    public void getCarApi() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<List<Car>> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Car>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars, response.getBody());
    }

    /**
     * Check that the /journey POST API return HttpStatus.ACCEPTED.
     * Post two journeys
     *
     * @throws Exception
     */
    @Test
    @Order(3)
    public void postJourneyApi() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response;
        HttpEntity<Object> entity;

        // journey 1
        entity = new HttpEntity<Object>(journeys.get(0), headers);
        response = restTemplate.exchange(
                "http://localhost:" + port + "/journey",
                HttpMethod.POST,
                entity,
                String.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        // journey 2
        entity = new HttpEntity<Object>(journeys.get(1), headers);
        response = restTemplate.exchange(
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
    public void postJourneyApiInvalidRequestBody() throws Exception {
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

    /**
     * Check that the /locate POST API return HttpStatus.Ok.
     *
     * @throws Exception
     */
    @Test
    @Order(4)
    public void postLocateApiStatusOK() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "0");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars.get(1).getId(), response.getBody().getId());
    }

    /**
     * Check that the /locate POST API return HttpStatus.NO_CONTENT.
     *
     * @throws Exception
     */
    @Test
    @Order(5)
    public void postLocateApiStatusNoContent() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "1");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.NOT_FOUND.
     *
     * @throws Exception
     */
    @Test
    public void postLocateApiStatusNotFound() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.BAD_REQUEST.
     * The payload can't be unmarshalled, wrong key value.
     *
     * @throws Exception
     */
    @Test
    public void postLocateApiStatusBadRequestWrongKeyValue() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("wrongKey", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.BAD_REQUEST.
     * The key "ID" appears several times.
     *
     * @throws Exception
     */
    @Test
    public void postLocateApiStatusBadRequestDuplicateKey() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.Ok.
     *
     * @throws Exception
     */
    @Test
    @Order(6)
    public void postDropOffApiStatusOK() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "0");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.NOT_FOUND.
     *
     * @throws Exception
     */
    @Test
    public void postDropOffApiStatusNotFound() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.BAD_REQUEST.
     * The payload can't be unmarshalled, wrong key value.
     *
     * @throws Exception
     */
    @Test
    public void postDropOffApiStatusBadRequestWrongKeyValue() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("wrongKey", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.BAD_REQUEST.
     * The key "ID" appears several times.
     *
     * @throws Exception
     */
    @Test
    public void postDropOffApiStatusBadRequestDuplicateKey() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.Ok.
     * For the Journey ID 1, this journey was waiting to be assigned to a car.
     *
     * @throws Exception
     */
    @Test
    @Order(7)
    public void postLocateApiStatusOKAfterWaiting() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "1");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars.get(1).getId(), response.getBody().getId());
    }

}
